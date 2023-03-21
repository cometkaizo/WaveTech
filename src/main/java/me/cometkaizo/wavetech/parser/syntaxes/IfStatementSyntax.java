package me.cometkaizo.wavetech.parser.syntaxes;

import me.cometkaizo.logging.LogUtils;
import me.cometkaizo.wavetech.parser.structures.IfStatement;
import me.cometkaizo.wavetech.syntaxes.Syntax;
import me.cometkaizo.wavetech.syntaxes.SyntaxStructure;
import org.jetbrains.annotations.NotNull;

import static me.cometkaizo.wavetech.lexer.tokens.types.FunctionalKeyword.IF;
import static me.cometkaizo.wavetech.lexer.tokens.types.OperatorKeyword.L_PAREN;
import static me.cometkaizo.wavetech.lexer.tokens.types.OperatorKeyword.R_PAREN;

public class IfStatementSyntax extends Syntax<IfStatement> {

    private IfStatementSyntax() {

        LogUtils.debug();
        rootBuilder
                .then(IF)
                .then(L_PAREN)
                .then("condition", ExpressionSyntax::getInstance)
                .then(R_PAREN)
                .then("body", BlockSyntax::getInstance);
    }

    public static IfStatementSyntax getInstance() {
        return InstanceHolder.INSTANCE;
    }

    private static class InstanceHolder {
        static final IfStatementSyntax INSTANCE = new IfStatementSyntax();
    }

    @Override
    public @NotNull IfStatement getStructure(SyntaxStructure parent) {
        return new IfStatement();
    }
}
