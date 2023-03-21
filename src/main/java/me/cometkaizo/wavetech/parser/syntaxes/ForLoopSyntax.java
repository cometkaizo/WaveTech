package me.cometkaizo.wavetech.parser.syntaxes;

import me.cometkaizo.wavetech.parser.structures.ForLoopStatement;
import me.cometkaizo.wavetech.syntaxes.Syntax;
import me.cometkaizo.wavetech.syntaxes.SyntaxStructure;
import org.jetbrains.annotations.NotNull;

import static me.cometkaizo.wavetech.lexer.tokens.types.FunctionalKeyword.FOR;
import static me.cometkaizo.wavetech.lexer.tokens.types.OperatorKeyword.*;

public class ForLoopSyntax extends Syntax<ForLoopStatement> {

    private ForLoopSyntax() {
        rootBuilder
                .then(FOR)
                .then(L_PAREN)
                .optionally("init", VariableDeclarationSyntax::getNoSemicolonInstance)
                .then(SEMICOLON)
                .optionally("termination", ExpressionSyntax::getInstance)
                .then(SEMICOLON)
                .optionally("update", VariableAssignationSyntax::getNoSemicolonInstance)
                .then(R_PAREN)
                .then("body", BlockSyntax::getInstance);

    }

    public static ForLoopSyntax getInstance() {
        return InstanceHolder.INSTANCE;
    }

    private static class InstanceHolder {
        static final ForLoopSyntax INSTANCE = new ForLoopSyntax();
    }

    @Override
    public @NotNull ForLoopStatement getStructure(SyntaxStructure parent) {
        return new ForLoopStatement();
    }

}
