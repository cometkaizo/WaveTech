package me.cometkaizo.wavetech.parser.syntaxes;

import me.cometkaizo.wavetech.parser.nodes.DeclaredNode;
import me.cometkaizo.wavetech.parser.Parser;
import me.cometkaizo.wavetech.parser.syntaxes.nodes.Syntax;

import static me.cometkaizo.wavetech.lexer.tokens.types.PrimitiveOperator.SEMICOLON;

class SemicolonSyntax extends Syntax {
    {
        rootNode.then(SEMICOLON);
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
