package me.cometkaizo.wavetech.parser;

import me.cometkaizo.wavetech.lexer.CompilationException;
import me.cometkaizo.wavetech.lexer.tokens.Token;
import me.cometkaizo.wavetech.parser.syntaxes.nodes.Syntax;
import me.cometkaizo.wavetech.parser.syntaxes.nodes.SyntaxToString;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.stream.Collectors;

public class UnknownSyntaxException extends CompilationException {

    public UnknownSyntaxException(List<Syntax> expected, List<Token> actual) {
        super(createMessage(expected, actual));
    }

    public UnknownSyntaxException(List<Syntax> expected, List<Token> actual, Throwable cause) {
        super(createMessage(expected, actual), cause);
    }

    @NotNull
    private static String createMessage(List<Syntax> expected, List<Token> actual) {
        return "Actual syntax '" + actual + "' does not match any filtered syntaxes: \n" +
                expected.stream()
                        .map(syntax -> {
                            SyntaxToString representation = new SyntaxToString(syntax);

                            return "  FOR SYNTAX " + syntax.getClass().getName() + "\n" +
                                    representation.represent().indent(4);
                        })
                        .collect(Collectors.joining("\n\n"));
    }

}
