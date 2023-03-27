package me.cometkaizo.wavetech.parser.syntaxes;

import me.cometkaizo.wavetech.lexer.tokens.types.OperatorKeyword;
import me.cometkaizo.wavetech.parser.structures.VariableAssignation;
import me.cometkaizo.wavetech.syntaxes.Syntax;
import me.cometkaizo.wavetech.syntaxes.SyntaxStructure;
import org.jetbrains.annotations.NotNull;

import static me.cometkaizo.wavetech.lexer.tokens.types.OperatorKeyword.*;

public class VariableAssignationSyntax extends Syntax<VariableAssignation> {

    private static final OperatorKeyword[] operators = {
            EQUALS,
            ASTERISK_EQUALS,
            SLASH_EQUALS,
            PERCENT_EQUALS,
            PLUS_EQUALS,
            MINUS_EQUALS,
            L_SHIFT_EQUALS,
            R_SHIFT_EQUALS,
            TRIPLE_R_SHIFT_EQUALS,
            AMPERSAND_EQUALS,
            CARET_EQUALS,
            PIPE_EQUALS
    };

    private VariableAssignationSyntax(boolean endWithSemicolon) {

        var builder = rootBuilder
                .then("variableAccessor", VariableAccessSyntax::getInstance)
                .split("operation", operators)
                .then("value", ExpressionSyntax::getInstance);

        if (endWithSemicolon)
            builder.then(SEMICOLON);

    }

    public static VariableAssignationSyntax getInstance() {
        return InstanceHolder.INSTANCE;
    }
    public static VariableAssignationSyntax getNoSemicolonInstance() {
        return InstanceHolder.NO_SEMICOLON_INSTANCE;
    }

    private static class InstanceHolder {
        static final VariableAssignationSyntax INSTANCE = new VariableAssignationSyntax(true);
        static final VariableAssignationSyntax NO_SEMICOLON_INSTANCE = new VariableAssignationSyntax(false);
    }

    @Override
    public @NotNull VariableAssignation getStructure(SyntaxStructure parent) {
        return new VariableAssignation();
    }
}
