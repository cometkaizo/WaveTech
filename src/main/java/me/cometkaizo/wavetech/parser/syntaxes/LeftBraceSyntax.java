package me.cometkaizo.wavetech.parser.syntaxes;

import me.cometkaizo.wavetech.parser.Parser;
import me.cometkaizo.wavetech.parser.nodes.DeclaredNode;
import me.cometkaizo.wavetech.parser.syntaxes.nodes.Syntax;
import me.cometkaizo.wavetech.parser.syntaxes.visitors.DepthIncreaseParserVisitor;
import me.cometkaizo.wavetech.parser.syntaxes.visitors.ParserStatusVisitor;

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
