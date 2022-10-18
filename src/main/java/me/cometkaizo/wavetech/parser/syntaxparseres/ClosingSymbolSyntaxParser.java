package me.cometkaizo.wavetech.parser.syntaxparseres;

import me.cometkaizo.wavetech.parser.Parser;
import me.cometkaizo.wavetech.parser.nodes.DeclaredNode;

import static me.cometkaizo.wavetech.lexer.tokens.types.PrimitiveOperator.R_BRACE;
import static me.cometkaizo.wavetech.lexer.tokens.types.PrimitiveOperator.R_PAREN;

public class ClosingSymbolSyntaxParser extends SyntaxParser {

    public ClosingSymbolSyntaxParser() {
        addExpectedSyntax(new AnyMatcher(false,
                R_BRACE,
                R_PAREN));
    }

    @Override
    protected boolean isValidInStatus(Parser.ParserStatus status) {
        return true;
    }

    @Override
    protected boolean isValidInContext(DeclaredNode context) {
        return true;
    }
}
