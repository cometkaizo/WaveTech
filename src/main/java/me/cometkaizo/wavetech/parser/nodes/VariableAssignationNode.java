package me.cometkaizo.wavetech.parser.nodes;

public class VariableAssignationNode extends Node {
    public final String varName;
    public final OperationNode expression;

    public VariableAssignationNode(String varName, OperationNode expression) {
        this.varName = varName;
        this.expression = expression;
    }

    @Override
    public String toString() {
        return "VariableAssignationNode{" +
                "varName='" + varName + '\'' +
                ", expression=" + expression +
                '}';
    }
}
