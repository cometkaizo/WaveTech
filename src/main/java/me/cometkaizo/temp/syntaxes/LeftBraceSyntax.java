package me.cometkaizo.temp.syntaxes;

import me.cometkaizo.temp.Parser;
import me.cometkaizo.temp.nodes.DeclaredNode;
import me.cometkaizo.temp.syntaxes.nodes.Syntax;
import me.cometkaizo.temp.syntaxes.visitors.DepthIncreaseParserVisitor;
import me.cometkaizo.temp.syntaxes.visitors.ParserStatusVisitor;

import static me.cometkaizo.wavetech.lexer.tokens.types.PrimitiveOperator.L_BRACE;

public class LeftBraceSyntax extends Syntax {
    public LeftBraceSyntax() {
        rootNode.then(
                L_BRACE
        );
    }

    @Override
    public ParserStatusVisitor createVisitor(DeclaredNode containingToken) {
        return new DepthIncreaseParserVisitor();
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
