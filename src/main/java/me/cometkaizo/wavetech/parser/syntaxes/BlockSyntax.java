package me.cometkaizo.wavetech.parser.syntaxes;

import me.cometkaizo.wavetech.parser.structures.MethodBlock;
import me.cometkaizo.wavetech.syntaxes.Syntax;
import me.cometkaizo.wavetech.syntaxes.SyntaxStructure;
import org.jetbrains.annotations.NotNull;

import static me.cometkaizo.wavetech.lexer.tokens.types.OperatorKeyword.L_BRACE;
import static me.cometkaizo.wavetech.lexer.tokens.types.OperatorKeyword.R_BRACE;

public class BlockSyntax extends Syntax<MethodBlock> {
    private BlockSyntax() {

        rootBuilder
                .then(L_BRACE)
                .zeroOrMore("content",
                        VariableDeclarationSyntax::getInstance,
                        VariableAssignationSyntax::getInstance,
                        IfStatementSyntax::getInstance,
                        ForLoopSyntax::getInstance,
                        WhileLoopSyntax::getInstance,
                        MethodAccessSyntax::getInstance
                )
                .then(R_BRACE);
    }

    public static @NotNull BlockSyntax getInstance() {
        return InstanceHolder.INSTANCE;
    }

    private static class InstanceHolder {
        static final BlockSyntax INSTANCE = new BlockSyntax();
    }

    @Override
    public @NotNull MethodBlock getStructure(SyntaxStructure parent) {
        return new MethodBlock();
    }
}
