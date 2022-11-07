package me.cometkaizo.wavetech.parser.syntaxes.visitors;

import me.cometkaizo.wavetech.parser.Parser;
import me.cometkaizo.wavetech.parser.nodes.DeclaredNode;
import me.cometkaizo.wavetech.parser.nodes.OperationNode;
import me.cometkaizo.wavetech.parser.nodes.VariableAssignationNode;

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
