package me.cometkaizo.wavetech.parser.diagnostics;

import me.cometkaizo.wavetech.lexer.tokens.Token;
import me.cometkaizo.wavetech.syntaxes.SyntaxNode;

import java.util.List;
import java.util.stream.Collectors;

public class NotASyntaxDiagnostic extends SyntaxErrorDiagnostic {
    public NotASyntaxDiagnostic(SyntaxNode expected, List<Token> actual) {
        this(List.of(expected), actual);
    }
    public NotASyntaxDiagnostic(List<SyntaxNode> expected, List<Token> actual) {
        super(createMessage(expected, actual), actual.get(0).start, actual.get(actual.size() - 1).end);
    }

    private static String createMessage(List<SyntaxNode> expected, List<Token> actual) {
        String expectedNodes = expected.stream().map(SyntaxNode::toPrettyString).collect(Collectors.joining(" or "));
        String actualTokens = actual.stream().map(token -> token.value == null ? token.type.symbol() : String.valueOf(token.value)).collect(Collectors.joining(" "));
        return "Expected: " + expectedNodes + "\nFound: " + actualTokens;
    }
}
