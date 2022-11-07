package me.cometkaizo.temp.syntaxes.visitors;

import me.cometkaizo.temp.Parser;
import me.cometkaizo.temp.nodes.DeclaredNode;
import me.cometkaizo.temp.nodes.OperationNode;
import me.cometkaizo.temp.nodes.VariableAssignationNode;

public class VariableAssignationParserVisitor extends ContainedParserVisitor {

    private final String varName;
    private final OperationNode expression;

    public VariableAssignationParserVisitor(DeclaredNode containingToken, String varName, OperationNode expression) {
        super(containingToken);
        this.varName = varName;
        this.expression = expression;
    }

    @Override
    public void visit(Parser parser) {
        containingToken.addNode(new VariableAssignationNode(varName, expression));
    }
}
