package me.cometkaizo.wavetech.parser.syntaxparseres;

import me.cometkaizo.wavetech.parser.nodes.DeclaredNode;
import me.cometkaizo.wavetech.parser.nodes.Node;
import me.cometkaizo.wavetech.parser.nodes.NodeType;

public abstract class DeclarationSyntaxParser extends SyntaxParser {

    public abstract Node create(DeclaredNode containingToken);

    public abstract NodeType getOperationType();

    public abstract boolean hasBody();

}
