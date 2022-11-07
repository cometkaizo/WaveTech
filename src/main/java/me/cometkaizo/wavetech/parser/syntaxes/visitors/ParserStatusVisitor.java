package me.cometkaizo.wavetech.parser.syntaxes.visitors;

import me.cometkaizo.wavetech.parser.Parser;

public interface ParserStatusVisitor {

    void visit(Parser parser);

}
