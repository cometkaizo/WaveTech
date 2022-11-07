package me.cometkaizo.temp.syntaxes.visitors;

import me.cometkaizo.temp.Parser;

public class DepthIncreaseParserVisitor implements ParserStatusVisitor {
    @Override
    public void visit(Parser parser) {
        parser.status.depth ++;
    }
}
