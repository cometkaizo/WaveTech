package me.cometkaizo.wavetech.parser.syntaxes;

import me.cometkaizo.wavetech.parser.structures.AssignationExtension;
import me.cometkaizo.wavetech.syntaxes.Syntax;
import me.cometkaizo.wavetech.syntaxes.SyntaxStructure;
import org.jetbrains.annotations.NotNull;

import static me.cometkaizo.wavetech.lexer.tokens.types.OperatorKeyword.*;

public class AssignationExtensionSyntax extends Syntax<AssignationExtension> {

    private AssignationExtensionSyntax(boolean endWithSemicolon) {
        var builder = rootBuilder
                .split("operation",
                        EQUALS,
                        PLUS_ASSIGN,
                        MINUS_ASSIGN,
                        ASTERISK_ASSIGN,
                        DOUBLE_ASTERISK_ASSIGN,
                        SLASH_ASSIGN
                )
                .then("value", ExpressionSyntax::getInstance);

        if (endWithSemicolon)
                builder.then(SEMICOLON);
    }

    public static AssignationExtensionSyntax getInstance() {
        return InstanceHolder.INSTANCE;
    }
    public static AssignationExtensionSyntax getNoSemicolonInstance() {
        return InstanceHolder.NO_SEMICOLON_INSTANCE;
    }

    private static class InstanceHolder {
        static final AssignationExtensionSyntax INSTANCE = new AssignationExtensionSyntax(true);
        static final AssignationExtensionSyntax NO_SEMICOLON_INSTANCE = new AssignationExtensionSyntax(false);
    }

    @Override
    public @NotNull AssignationExtension getStructure(SyntaxStructure parent) {
        return new AssignationExtension();
    }

}
