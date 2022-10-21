package me.cometkaizo.wavetech.parser.syntaxes;

import me.cometkaizo.wavetech.parser.syntaxes.nodes.Syntax;
import me.cometkaizo.wavetech.parser.nodes.DeclaredNode;
import me.cometkaizo.wavetech.parser.nodes.Node;
import me.cometkaizo.wavetech.parser.nodes.NodeType;

public abstract class DeclarationSyntax extends Syntax {

    public abstract Node create(DeclaredNode containingToken);

    public abstract NodeType getOperationType();

    public abstract boolean hasBody();

}
