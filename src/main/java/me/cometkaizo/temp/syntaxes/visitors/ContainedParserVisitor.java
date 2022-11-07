package me.cometkaizo.temp.syntaxes.visitors;

import me.cometkaizo.temp.nodes.DeclaredNode;

public abstract class ContainedParserVisitor implements ParserStatusVisitor {

    protected final DeclaredNode containingToken;

    public ContainedParserVisitor(DeclaredNode containingToken) {
        this.containingToken = containingToken;
    }

}
