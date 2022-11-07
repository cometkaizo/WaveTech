package me.cometkaizo.wavetech.parser.syntaxes.visitors;

import me.cometkaizo.wavetech.parser.Parser;

public class DepthIncreaseParserVisitor implements ParserStatusVisitor {
    @Override
    public void visit(Parser parser) {
        parser.status.depth ++;
    }
}
