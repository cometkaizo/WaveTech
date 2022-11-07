package me.cometkaizo.wavetech.parser.syntaxes;

import me.cometkaizo.wavetech.parser.Parser;
import me.cometkaizo.wavetech.parser.nodes.DeclaredNode;
import me.cometkaizo.wavetech.parser.syntaxes.nodes.Syntax;
import me.cometkaizo.wavetech.parser.syntaxes.visitors.DepthDecreaseParserVisitor;
import me.cometkaizo.wavetech.parser.syntaxes.visitors.ParserStatusVisitor;

import static me.cometkaizo.wavetech.lexer.tokens.types.PrimitiveOperator.R_BRACE;

public class RightBraceSyntax extends Syntax {

    public RightBraceSyntax() {
        rootNode.then(
                R_BRACE
        );
    }

    @Override
    public ParserStatusVisitor createVisitor(DeclaredNode containingToken) {
        return new DepthDecreaseParserVisitor();
    }

    @Override
    protected boolean isValidInStatus(Parser.Status status) {
        return status.depth > 0;
    }

    @Override
    protected boolean isValidInContext(DeclaredNode context) {
        return true;
    }
}
