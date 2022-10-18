package me.cometkaizo.wavetech.parser;

import me.cometkaizo.util.LogUtils;
import me.cometkaizo.wavetech.lexer.CompilationException;
import me.cometkaizo.wavetech.lexer.tokens.Token;
import me.cometkaizo.wavetech.lexer.tokens.TokenType;
import me.cometkaizo.wavetech.lexer.tokens.types.*;
import me.cometkaizo.wavetech.parser.syntaxparseres.ClosingSymbolSyntaxParser;
import me.cometkaizo.wavetech.parser.syntaxparseres.DeclarationSyntaxParser;
import me.cometkaizo.wavetech.parser.syntaxparseres.SyntaxParser;
import me.cometkaizo.wavetech.parser.syntaxparseres.SyntaxParsers;
import me.cometkaizo.wavetech.parser.nodes.DeclaredNode;
import me.cometkaizo.wavetech.parser.nodes.SourceFileNode;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class Parser {

    private final ListIterator<Token> tokens;

    private final ParserStatus status = new ParserStatus();

    public static class ParserStatus {

        public Token token = null;
        public final SourceFileNode sourceFileToken = new SourceFileNode("DEFAULT_NAME IN NEW_PARSER.JAVA");

        public int depth = 0;

        private final List<Token> tokenBuffer = new ArrayList<>();

        private List<SyntaxParser> nextExpectedParsers = new ArrayList<>();
        private List<SyntaxParser> currentValidParsers = new ArrayList<>();
        private List<SyntaxParser> currentExactParsers = new ArrayList<>();
    }

    private static final List<Supplier<SyntaxParser>> allMatcherConstructors = SyntaxParsers.syntaxMatcherCreators;



    public Parser(List<Token> tokenList) {
        this.tokens = tokenList.listIterator();
    }

    private void reset() {
        status.token = null;
        status.tokenBuffer.clear();

        status.currentValidParsers.clear();
    }



    public SourceFileNode parse() {
        status.depth = 0;

        parseInContext(status.sourceFileToken);
        return status.sourceFileToken;
    }



    private void parseInContext(DeclaredNode context) {
        int startingDepth = status.depth;
        boolean depthChanged = false;

        while (tokens.hasNext()) {
            status.token = tokens.next();
            parseToken(context);

            if (depthChanged && status.depth == startingDepth) return;
            depthChanged = status.depth > startingDepth;
        }

        if (status.depth > startingDepth) throw new CompilationException("Expected '}'");
    }

    private void parseToken(DeclaredNode context) {
        Token token = status.token;
        LogUtils.info("Found token '{}'", token);

        readToken(token);

        updateSyntaxMatchers(context);

        LogUtils.success("Expected syntax: {}", status.nextExpectedParsers);
        LogUtils.success("All syntaxes matching {}: {}", status.tokenBuffer, status.currentValidParsers.stream().map(o -> o.getClass().getSimpleName()).toList());
        LogUtils.success("Exact syntaxes matching {}: {}\n", status.tokenBuffer, status.currentExactParsers.stream().map(o -> o.getClass().getSimpleName()).toList());

        if (status.nextExpectedParsers.size() > 0) {
            handleExpectedMatchers(context);
        } else {
            handleMatchers(context);
        }
    }

    private void updateSyntaxMatchers(DeclaredNode context) {
        if (status.nextExpectedParsers.size() > 0) return;

        if (status.currentValidParsers.size() == 0) {
            status.currentValidParsers = createNewParsers();
            LogUtils.info("There were no valid parsers, created new matchers {}", status.currentValidParsers);
        }

        List<SyntaxParser> newValidParsers = new ArrayList<>();
        List<SyntaxParser> newExactParsers = new ArrayList<>();

        for (SyntaxParser syntaxParser : status.currentValidParsers) {
            SyntaxParser.Result matchResult = syntaxParser.matchNext(context, status, status.token);

            switch (matchResult) {
                case MATCHES_SO_FAR -> {
                    LogUtils.info("Added new valid syntax matcher {} because it matches {} so far", syntaxParser, status.token);
                    newValidParsers.add(syntaxParser);
                }
                case MATCHES_EXACT -> {
                    LogUtils.info("Added new exact and valid syntax matcher {} because it matches {} so far", syntaxParser, status.token);
                    newValidParsers.add(syntaxParser);
                    newExactParsers.add(syntaxParser);
                }
            }
        }
        status.currentValidParsers = newValidParsers;
        status.currentExactParsers = newExactParsers;
    }

    private void handleExpectedMatchers(DeclaredNode context) {
        if (status.nextExpectedParsers.size() == 0) return;

        List<SyntaxParser> newValidExpectedParsers = new ArrayList<>();

        for (SyntaxParser expectedParser : status.nextExpectedParsers) {
            SyntaxParser.Result matchResult = expectedParser.matchNext(context, status, status.token);

            if (matchResult == SyntaxParser.Result.MATCHES_EXACT) {
                status.nextExpectedParsers.clear();
                reset();
                return;
            } else if (matchResult == SyntaxParser.Result.MATCHES_SO_FAR) {
                newValidExpectedParsers.add(expectedParser);
            }
        }

        if (newValidExpectedParsers.size() == 0)
            throw new CompilationException("Actual syntax '" + status.tokenBuffer + "' does not match any expected syntax:\n" +
                    status.nextExpectedParsers.stream()
                            .map(syntaxParser -> syntaxParser.getExpectedPatterns().stream().map(Arrays::toString).toList())
                            .toList());

        status.nextExpectedParsers = newValidExpectedParsers;
    }

    private void handleMatchers(DeclaredNode context) {
        throwIfNoValidMatchers(context);

        if (status.currentExactParsers.size() > 1) {
            throw new IllegalStateException("Encountered multiple syntax matchers matching pattern '" + status.tokenBuffer + "', \n" + status.currentExactParsers);
        } else if (status.currentExactParsers.size() == 1) {
            SyntaxParser syntaxParser = status.currentExactParsers.get(0);
            LogUtils.info("Found exact matching matcher {}", syntaxParser);

            handleExactMatcher(context, syntaxParser);

            reset();
        }
    }

    private void handleExactMatcher(DeclaredNode context, SyntaxParser syntaxMatcher) {

        status.nextExpectedParsers = syntaxMatcher.getNextExpectedPatterns();

        if (syntaxMatcher instanceof DeclarationSyntaxParser declarationSyntaxMatcher) {

            LogUtils.info("exact pattern index: {}", declarationSyntaxMatcher.getExactMatchingInputPatternIndex());

            DeclaredNode newToken = (DeclaredNode) declarationSyntaxMatcher.create(context);
            context.addNode(declarationSyntaxMatcher.getOperationType(), newToken);

            reset();
            if (declarationSyntaxMatcher.hasBody()) {
                parseInContext(newToken);
            }

        } else if (syntaxMatcher instanceof ClosingSymbolSyntaxParser) {

        } else {
            LogUtils.warn("SyntaxMatcher without use: {}", syntaxMatcher);
        }
    }

    private void throwIfNoValidMatchers(DeclaredNode context) {
        if (status.currentValidParsers.size() == 0) {

            throw new CompilationException("Unknown syntax " + status.tokenBuffer + " in " + context.getClass().getSimpleName() + " context");

        }
    }

    @NotNull
    private static List<SyntaxParser> createNewParsers() {
        return allMatcherConstructors.stream().map(Supplier::get).collect(Collectors.toList());
    }

    private void readToken(Token token) {

        status.tokenBuffer.add(token);
        TokenType type = token.getType();
        Object value = token.getValue();

        LogUtils.info("Read token '{}{}'", token.getType(), value == null? "" : " " + value);

        if (type instanceof PrimitiveOperator parsedPrimitiveOperator) {

            switch (parsedPrimitiveOperator) {
                case L_BRACE -> {
                    status.depth++;
                    LogUtils.info("depth is now: {}", status.depth);
                }
                case R_BRACE -> {
                    if (status.depth == 0) throw new CompilationException("Unexpected '}' without '{'");
                    status.depth--;
                }
            }

        }
    }

}
