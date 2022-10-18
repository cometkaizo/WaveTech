package me.cometkaizo.wavetech.parser.nodes;

public class BinaryNode<T> extends Node {

    protected final NodeType type;
    protected T value;

    protected BinaryNode(NodeType type, T value) {
        super();
        this.type = type;
        this.value = value;
    }

    public NodeType getType() {
        return type;
    }

    public T getValue() {
        return value;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "{" +
                "type=" + type +
                ", value=" + value +
                '}';
    }
}
