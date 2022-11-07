package me.cometkaizo.temp.nodes;

public class ClassDeclarationNode extends Node {

    public final ClassNode classNode;

    public ClassDeclarationNode(ClassNode classNode) {
        this.classNode = classNode;
    }

    @Override
    public String toString() {
        return "ClassDeclarationNode{" +
                "classNode=" + classNode +
                '}';
    }

}
