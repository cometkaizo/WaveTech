package me.cometkaizo.wavetech.parser.syntaxes.visitors;

import me.cometkaizo.wavetech.parser.nodes.DeclaredNode;

public abstract class ContainedParserVisitor implements ParserStatusVisitor {

    protected final DeclaredNode containingToken;

    public ContainedParserVisitor(DeclaredNode containingToken) {
        this.containingToken = containingToken;
    }

}
