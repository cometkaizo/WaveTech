package me.cometkaizo.wavetech.parser;

import me.cometkaizo.util.LogUtils;
import me.cometkaizo.wavetech.lexer.CompilationException;
import me.cometkaizo.wavetech.lexer.tokens.Token;
import me.cometkaizo.wavetech.lexer.tokens.TokenType;
import me.cometkaizo.wavetech.lexer.tokens.types.PrimitiveOperator;
import me.cometkaizo.wavetech.parser.syntaxes.nodes.Syntax;
import me.cometkaizo.wavetech.parser.nodes.DeclaredNode;
import me.cometkaizo.wavetech.parser.nodes.SourceFileNode;
import me.cometkaizo.wavetech.parser.syntaxes.ClosingSymbolSyntax;
import me.cometkaizo.wavetech.parser.syntaxes.DeclarationSyntax;
import me.cometkaizo.wavetech.parser.syntaxes.Syntaxes;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class Parser {

    private final ListIterator<Token> tokens;
    public final SourceFileNode sourceFileToken = new SourceFileNode("DEFAULT_NAME");

    private final Status status = new Status();

    public static class Status {

        public Token token = null;

        public int depth = 0;

        private final List<Token> tokenBuffer = new ArrayList<>();

        private List<Syntax> currentValidSyntaxes = createNewSyntaxes();
        private List<Syntax> currentExactSyntaxes = new ArrayList<>();

        private void reset() {
            token = null;
            tokenBuffer.clear();

            currentExactSyntaxes.clear();
            currentValidSyntaxes.clear();
        }
    }

    private static final List<Supplier<Syntax>> allMatcherConstructors = Syntaxes.syntaxCreators;



    public Parser(List<Token> tokenList) {
        this.tokens = tokenList.listIterator();
    }


    public SourceFileNode parse() {
        status.depth = 0;

        parseInContext(sourceFileToken);
        return sourceFileToken;
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

        updateSyntaxes(context);

        LogUtils.success("All syntaxes matching {}: {}", status.tokenBuffer, status.currentValidSyntaxes.stream().map(o -> o.getClass().getSimpleName()).toList());
        LogUtils.success("Exact syntaxes matching {}: {}\n", status.tokenBuffer, status.currentExactSyntaxes.stream().map(o -> o.getClass().getSimpleName()).toList());

        handleSyntaxes(context);
    }

    private void updateSyntaxes(DeclaredNode context) {

        if (status.currentValidSyntaxes.size() == 0) {
            status.currentValidSyntaxes = createNewSyntaxes();
            LogUtils.info("There were no valid syntaxes, created new syntaxes");
        }

        List<Syntax> newValidSyntaxes = new ArrayList<>();
        List<Syntax> newExactSyntaxes = new ArrayList<>();

        for (Syntax syntax : status.currentValidSyntaxes) {
            //LogUtils.debug("trying to match syntax {} with {}", syntax, status.token);
            Syntax.Result matchResult = syntax.matchNext(context, status, status.token);

            switch (matchResult) {
                case MATCHES_SO_FAR -> {
                    LogUtils.info("Added new valid syntax matcher {} because it matches {} so far", syntax, status.token);
                    newValidSyntaxes.add(syntax);
                }
                case MATCHES_EXACT -> {
                    LogUtils.info("Added new exact and valid syntax matcher {} because it matches {} so far", syntax, status.token);
                    newValidSyntaxes.add(syntax);
                    newExactSyntaxes.add(syntax);
                }
            }
        }

        throwIfNoNextValidSyntaxes(newValidSyntaxes);

        status.currentValidSyntaxes = newValidSyntaxes;
        status.currentExactSyntaxes = newExactSyntaxes;
    }

    private void throwIfNoNextValidSyntaxes(List<Syntax> newValidSyntaxes) {
        if (newValidSyntaxes.size() == 0) {
            throw new UnknownSyntaxException(status.currentValidSyntaxes, status.tokenBuffer);
        }
    }

    private void handleSyntaxes(DeclaredNode context) {
        throwIfNoValidSyntaxes(context);

        if (status.currentExactSyntaxes.size() > 1) {
            throw new IllegalStateException("Encountered multiple syntax matchers matching pattern '" + status.tokenBuffer + "', \n" + status.currentExactSyntaxes);
        } else if (status.currentExactSyntaxes.size() == 1) {
            Syntax syntax = status.currentExactSyntaxes.get(0);
            LogUtils.info("Found exact syntax {}", syntax);

            handleExactSyntax(context, syntax);

        }
    }

    private void handleExactSyntax(DeclaredNode context, Syntax syntax) {

        status.reset();

        status.currentValidSyntaxes = syntax.getNextExpectedSyntaxes();

        if (syntax instanceof DeclarationSyntax declarationSyntaxMatcher) {

            DeclaredNode newToken = (DeclaredNode) declarationSyntaxMatcher.create(context);
            context.addNode(declarationSyntaxMatcher.getOperationType(), newToken);
            if (declarationSyntaxMatcher.hasBody()) {
                parseInContext(newToken);
            }

        } else if (syntax instanceof ClosingSymbolSyntax) {

        } else {
            LogUtils.warn("Unused Syntax: {}", syntax);
        }
    }

    private void throwIfNoValidSyntaxes(DeclaredNode context) {
        if (status.currentValidSyntaxes.size() == 0) {
            throw new CompilationException("Unknown syntax " + status.tokenBuffer + " in " + context.getClass().getSimpleName() + " context");
        }
    }

    @NotNull
    private static List<Syntax> createNewSyntaxes() {
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
