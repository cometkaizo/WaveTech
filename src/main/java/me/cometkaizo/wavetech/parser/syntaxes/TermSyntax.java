package me.cometkaizo.wavetech.parser.syntaxes;

import me.cometkaizo.util.LogUtils;
import me.cometkaizo.util.PeekingIterator;
import me.cometkaizo.wavetech.lexer.tokens.Token;
import me.cometkaizo.wavetech.lexer.tokens.types.PrimitiveOperator;
import me.cometkaizo.wavetech.parser.Parser;
import me.cometkaizo.wavetech.parser.nodes.BinaryOperationNode;
import me.cometkaizo.wavetech.parser.nodes.DeclaredNode;
import me.cometkaizo.wavetech.parser.nodes.OperationNode;
import me.cometkaizo.wavetech.parser.syntaxes.nodes.MatcherFunction;
import me.cometkaizo.wavetech.parser.syntaxes.nodes.Syntax;
import me.cometkaizo.wavetech.parser.syntaxes.nodes.SyntaxToString;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class TermSyntax extends Syntax {

    public static final List<PrimitiveOperator> validExpressionOperators = List.of(
            PrimitiveOperator.ASTERISK,
            PrimitiveOperator.SLASH,
            PrimitiveOperator.PERCENT
    );

    protected List<FactorSyntax> factors = new ArrayList<>();
    protected List<PrimitiveOperator> operators = new ArrayList<>();

    public TermSyntax() {

        rootNode
                .forEachNextToken(new MatcherFunction("factors") {

                    boolean expectPrimitiveOperator = false;

                    @Override
                    public @NotNull Result match(@NotNull Token token, @Nullable Token nextToken) {


                        if (expectPrimitiveOperator) {
                            if (!isValidOperator(token)) return Result.NO_MATCH;
                            LogUtils.debug("primitive operator: {}", token.getType());
                            operators.add((PrimitiveOperator) token.getType());

                            expectPrimitiveOperator = false;
                        } else {
                            Result matchResult = matchNextFor(FactorSyntax::new, "factor");

                            if (matchResult == Result.MATCHES_SO_FAR)
                                return Result.MATCHES_SO_FAR;
                            if (matchResult == Result.NO_MATCH) {
                                syntaxes.remove("factor");
                                return Result.NO_MATCH;
                            }

                            FactorSyntax factor = (FactorSyntax) syntaxes.get("factor");
                            LogUtils.debug("found factor: {}, terms before: {}", factor, factors);
                            factors.add(factor);
                            LogUtils.debug("found factor: {}, terms after: {}", factor, factors);

                            syntaxes.remove("factor");
                            if (!isValidOperator(nextToken)) return Result.MATCHES_EXACT;

                            expectPrimitiveOperator = true;
                        }
                        return Result.MATCHES_SO_FAR;
                    }
                })
        ;

        LogUtils.debug("for {}, printed: \n{}", getClass().getName(), new SyntaxToString(this).represent());

    }

    static boolean isValidOperator(Token candidate) {
        if (candidate == null) return false;
        if (!(candidate.getType() instanceof PrimitiveOperator)) return false;
        return validExpressionOperators.contains((PrimitiveOperator) candidate.getType());
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
        return "TermSyntax{" +
                "factors=" + factors +
                ", operators=" + operators +
                '}';
    }

    @NotNull
    public OperationNode createOperationNode() {
        LogUtils.debug("----------- factors: {}", factors);
        if (factors.size() == 0) throw new IllegalStateException("Cannot create operation node without factors");

        LogUtils.success("Found: \n{}, \n{}", factors, operators);
        if (factors.size() == 1) return factors.get(0).createOperationNode();

        BinaryOperationNode currentOpNode = new BinaryOperationNode(operators.get(0),
                factors.get(0).createOperationNode(),
                factors.get(1).createOperationNode());

        PeekingIterator<FactorSyntax> factorPeeker = PeekingIterator.of(factors);
        factorPeeker.advance(2);

        while (factorPeeker.hasNext()) {
            PrimitiveOperator operator = operators.get(factorPeeker.cursor());
            FactorSyntax factor = factorPeeker.next();

            currentOpNode = new BinaryOperationNode(operator, currentOpNode, factor.createOperationNode());
        }

        LogUtils.success("Found: \n{}", currentOpNode);
        return currentOpNode;
    }
}
