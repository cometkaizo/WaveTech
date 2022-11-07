package me.cometkaizo.wavetech.parser.syntaxes;

import me.cometkaizo.wavetech.parser.Parser;
import me.cometkaizo.wavetech.parser.nodes.DeclaredNode;
import me.cometkaizo.wavetech.parser.syntaxes.nodes.Syntax;

import static me.cometkaizo.wavetech.lexer.tokens.types.PrimitiveOperator.L_PAREN;

public class LeftParenSyntax extends Syntax {
    public LeftParenSyntax() {
        rootNode.then(
                L_PAREN
        );
    }



    @Override
    protected boolean isValidInStatus(Parser.Status status) {
        return true;
    }

    @Override
    protected boolean isValidInContext(DeclaredNode context) {
        return true;
    }
}
