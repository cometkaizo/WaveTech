package me.cometkaizo.temp.syntaxes;

import me.cometkaizo.temp.Parser;
import me.cometkaizo.temp.nodes.DeclaredNode;
import me.cometkaizo.temp.syntaxes.nodes.Syntax;
import me.cometkaizo.temp.syntaxes.visitors.DepthDecreaseParserVisitor;
import me.cometkaizo.temp.syntaxes.visitors.ParserStatusVisitor;

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
