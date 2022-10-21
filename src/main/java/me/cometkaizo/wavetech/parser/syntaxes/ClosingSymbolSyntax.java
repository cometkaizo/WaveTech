package me.cometkaizo.wavetech.parser.syntaxes;

import me.cometkaizo.wavetech.parser.Parser;
import me.cometkaizo.wavetech.parser.syntaxes.nodes.Syntax;
import me.cometkaizo.wavetech.parser.nodes.DeclaredNode;

import static me.cometkaizo.wavetech.lexer.tokens.types.PrimitiveOperator.R_BRACE;
import static me.cometkaizo.wavetech.lexer.tokens.types.PrimitiveOperator.R_PAREN;

public class ClosingSymbolSyntax extends Syntax {

    public ClosingSymbolSyntax() {
        rootNode
                .split(R_BRACE, R_PAREN);
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
