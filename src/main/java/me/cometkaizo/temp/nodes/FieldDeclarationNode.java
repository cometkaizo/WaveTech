package me.cometkaizo.temp.nodes;

public class FieldDeclarationNode extends Node {

    public final FieldNode fieldNode;

    public FieldDeclarationNode(FieldNode fieldNode) {
        this.fieldNode = fieldNode;
    }

    @Override
    public String toString() {
        return "FieldDeclarationNode{" +
                "fieldNode=" + fieldNode +
                '}';
    }

}
