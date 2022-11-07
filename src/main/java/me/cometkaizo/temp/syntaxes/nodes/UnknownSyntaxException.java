package me.cometkaizo.temp.syntaxes.nodes;

import me.cometkaizo.temp.syntaxes.Syntaxes;
import me.cometkaizo.wavetech.lexer.CompilationException;
import me.cometkaizo.wavetech.lexer.tokens.Token;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.stream.Collectors;

public class UnknownSyntaxException extends CompilationException {

    public UnknownSyntaxException(List<Syntax> expected, List<Token> actual) {
        super('\n' + createMessage(expected, actual));
    }

    public UnknownSyntaxException(List<Syntax> expected, List<Token> actual, Throwable cause) {
        super('\n' + createMessage(expected, actual), cause);
    }

    @NotNull
    private static String createMessage(List<Syntax> expected, List<Token> actual) {
        if (expected.size() == 0) return "Actual syntax '" + actual + "' does not match any syntaxes (none given)";

        if (expected.size() == 1 && isSimple(expected.get(0)))
            return createSimpleMessage(expected.get(0), actual);

        if (expected.size() == Syntaxes.syntaxSuppliers.size())
            return createGenericMessage(actual);

        return createComplexMessage(expected, actual);
    }

    private static boolean isSimple(Syntax expected) {
        SyntaxNode expectedRootNode = expected.getRootOrBuild();
        int depth = expectedRootNode.getDepth();

        return depth < 2 || (expectedRootNode.splits && depth == 2);
    }

    @NotNull
    private static String createSimpleMessage(Syntax expected, List<Token> actual) {
        return "Expected '" +
                expected.getRootOrBuild().subNodes.stream()
                        .map(SyntaxNode::representData)
                        .collect(Collectors.joining(" or ")) +
                "' but found '" +
                actual.stream()
                        .map(token -> {
                            if (token.getValue() == null)
                                return token.getType().toString();
                            return token.getValue().toString();
                        })
                        .collect(Collectors.joining(", ")) + "'";
    }

    @NotNull
    private static String createGenericMessage(List<Token> actual) {
        return "Actual syntax '" + actual.stream()
                .map(token -> {
                    if (token.getValue() == null)
                        return token.getType().toString();
                    return token.getValue().toString();
                })
                .collect(Collectors.joining(", ")) + "' does not match any syntaxes";
    }


    @NotNull
    private static String createComplexMessage(List<Syntax> expected, List<Token> actual) {
        String actualStr = actual.stream()
                .map(token -> {
                    if (token.getValue() == null)
                        return token.getType().toString();
                    return token.getValue().toString();
                })
                .collect(Collectors.joining(", "));
        return "Actual syntax '" + actualStr + "' does not match any of the following syntaxes: \n\n" +
                expected.stream()
                        .map(syntax -> {
                            SyntaxToString representation = new SyntaxToString(syntax);
                            String[] fullSyntaxName = syntax.getClass().getName().split("\\.");
                            String syntaxName = fullSyntaxName[fullSyntaxName.length - 1];

                            return "  " + syntaxName + " SYNTAX IN " + syntax.getClass().getPackageName() + "\n\n" +
                                    representation.represent().indent(4);
                        })
                        .collect(Collectors.joining("\n"));
    }

}
