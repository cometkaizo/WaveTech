package me.cometkaizo.temp.syntaxes;

import me.cometkaizo.temp.Parser;
import me.cometkaizo.temp.nodes.DeclaredNode;
import me.cometkaizo.temp.nodes.OperationNode;
import me.cometkaizo.temp.nodes.UnaryOperationNode;
import me.cometkaizo.temp.syntaxes.nodes.ConditionalSyntaxNodeBuilder;
import me.cometkaizo.temp.syntaxes.nodes.Syntax;
import me.cometkaizo.temp.syntaxes.nodes.SyntaxToString;
import me.cometkaizo.util.LogUtils;
import me.cometkaizo.wavetech.lexer.tokens.Token;
import me.cometkaizo.wavetech.lexer.tokens.types.ObjectType;
import me.cometkaizo.wavetech.lexer.tokens.types.PrimitiveValue;

public class FactorSyntax extends Syntax {


    public FactorSyntax() {

        rootNode
                .executes(() -> LogUtils.debug("0-id=+"))
                .split(
                        new ConditionalSyntaxNodeBuilder(token -> {
                            boolean is = token.getType() instanceof PrimitiveValue;
                            LogUtils.debug("HELLLOOOOOOOO HALLLOOOOO, {} is instance of primitive value? {}", token, is);
                            return is;
                        }),
                        new ConditionalSyntaxNodeBuilder(token -> {
                            boolean is = token.getType() instanceof ObjectType;
                            LogUtils.debug("HELLLOOOOOOOO HALLLOOOOO - 2, {} is instance of object type? {}", token, is);
                            return is;
                        })
                )
                .executes(() -> LogUtils.debug("1-id=+"))
        ;

        LogUtils.debug("for {}, printed: \n{}", getClass().getName(), new SyntaxToString(this).represent());

    }

    public Token getValueToken() {
        var inputs = getInputTokens();
        LogUtils.debug("inputs: {}", inputs);

        if (inputs.containsKey(PrimitiveValue.class))
            return inputs.get(PrimitiveValue.class).get(0);
        if (inputs.containsKey(ObjectType.class)) {
            Token objectTypeToken = inputs.get(ObjectType.class).get(0);
            return new Token(ObjectType.REFERENCE, objectTypeToken.getValue());
        }
        throw new IllegalStateException("No value token found: " + inputs);
    }

    public boolean hasValidValueToken() {
        var inputs = getInputTokens();
        return inputs.containsKey(PrimitiveValue.class)
                || inputs.containsKey(ObjectType.class);
    }


    @Override
    protected boolean isValidInStatus(Parser.Status status) {
        return true;
    }

    @Override
    protected boolean isValidInContext(DeclaredNode context) {
        return true;
    }

    @Override
    public String toString() {
        return "FactorSyntax{" +
                "value=" + (hasValidValueToken()? getValueToken() : null) +
                '}';
    }

    public OperationNode createOperationNode() {
        Token valueToken = getValueToken();
        LogUtils.debug("value token: {}, inputs: {}", valueToken, getInputTokens());
        return new UnaryOperationNode(valueToken);
    }
}
