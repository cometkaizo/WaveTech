package me.cometkaizo.wavetech.parser.syntaxes;

import me.cometkaizo.wavetech.parser.structures.WhileLoopStatement;
import me.cometkaizo.wavetech.syntaxes.Syntax;
import me.cometkaizo.wavetech.syntaxes.SyntaxStructure;
import org.jetbrains.annotations.NotNull;

import static me.cometkaizo.wavetech.lexer.tokens.types.FunctionalKeyword.WHILE;
import static me.cometkaizo.wavetech.lexer.tokens.types.OperatorKeyword.L_PAREN;
import static me.cometkaizo.wavetech.lexer.tokens.types.OperatorKeyword.R_PAREN;

public class WhileLoopSyntax extends Syntax<WhileLoopStatement> {

    public WhileLoopSyntax() {
        rootBuilder
                .then(WHILE)
                .then(L_PAREN)
                .then("condition", ExpressionSyntax::getInstance)
                .then(R_PAREN)
                .then("body", BlockSyntax::getInstance)
        ;
    }

    public static WhileLoopSyntax getInstance() {
        return InstanceHolder.INSTANCE;
    }

    private static class InstanceHolder {
        static final WhileLoopSyntax INSTANCE = new WhileLoopSyntax();
    }

    @Override
    public @NotNull WhileLoopStatement getStructure(SyntaxStructure parent) {
        return new WhileLoopStatement();
    }
}
