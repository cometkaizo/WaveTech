package me.cometkaizo.wavetech.parser.syntaxes;

import me.cometkaizo.wavetech.lexer.tokens.types.ObjectType;
import me.cometkaizo.wavetech.lexer.tokens.types.PrimitiveOperator;
import me.cometkaizo.wavetech.parser.Parser;
import me.cometkaizo.wavetech.parser.nodes.DeclaredNode;
import me.cometkaizo.wavetech.parser.syntaxes.nodes.Syntax;
import me.cometkaizo.wavetech.parser.syntaxes.visitors.ParserStatusVisitor;
import me.cometkaizo.wavetech.parser.syntaxes.visitors.VariableAssignationParserVisitor;

import static me.cometkaizo.wavetech.lexer.tokens.types.ObjectType.*;

class VariableAssignationSyntax extends Syntax {

    protected String varName = null;
    protected final ExpressionSyntax expressionSyntax = new ExpressionSyntax();

    public VariableAssignationSyntax() {

        rootNode
                .then(SYMBOL_OR_REFERENCE, REFERENCE)
                .executes(() ->
                        varName = (String) getInputTokens().get(ObjectType.class).stream()
                                .filter(token -> token.getType() == SYMBOL)
                                .findFirst().orElseThrow()
                                .getValue()
                )
                .then(PrimitiveOperator.EQUALS)
                .thenSyntax(expressionSyntax)
        ;

        addNextExpectedSyntax(new SemicolonSyntax());

    }
    public VariableAssignationSyntax(String varName) {
        this.varName = varName;

        rootNode
                .then(PrimitiveOperator.EQUALS)
                .thenSyntax(expressionSyntax)
        ;

        addNextExpectedSyntax(new SemicolonSyntax());

    }

    @Override
    public ParserStatusVisitor createVisitor(DeclaredNode containingToken) {
        if (varName == null) throw new IllegalStateException();
        return new VariableAssignationParserVisitor(containingToken, varName, expressionSyntax.createOperationNode());
    }

    @Override
    protected boolean isValidInStatus(Parser.Status status) {
        return true;
    }

    @Override
    protected boolean isValidInContext(DeclaredNode context) {
        return true;
    }
}
