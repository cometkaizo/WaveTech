package me.cometkaizo.temp.syntaxes;

import me.cometkaizo.temp.Parser;
import me.cometkaizo.temp.nodes.DeclaredNode;
import me.cometkaizo.temp.syntaxes.nodes.Syntax;

import static me.cometkaizo.wavetech.lexer.tokens.types.PrimitiveOperator.R_PAREN;

public class RightParenSyntax extends Syntax {
    public RightParenSyntax() {
        rootNode.then(
                R_PAREN
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
