package me.cometkaizo.wavetech.parser.syntaxes.visitors;

import me.cometkaizo.wavetech.parser.Parser;

public class DepthDecreaseParserVisitor implements ParserStatusVisitor {
    @Override
    public void visit(Parser parser) {
        parser.status.depth --;
    }
}
