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

// TODO: 2022-10-24 Add syntax for multiple terms as well as operators

public class ExpressionSyntax extends Syntax {

    public static final List<PrimitiveOperator> validExpressionOperators = List.of(
            PrimitiveOperator.PLUS,
            PrimitiveOperator.MINUS
    );

    protected final List<TermSyntax> terms = new ArrayList<>();
    protected final List<PrimitiveOperator> operators = new ArrayList<>();

    public ExpressionSyntax() {

        rootNode
                .forEachNextToken(new MatcherFunction("terms") {

                    boolean expectPrimitiveOperator = false;

                    @Override
                    public @NotNull Result match(@NotNull Token token, @Nullable Token nextToken) {

                        if (expectPrimitiveOperator) {
                            if (!isValidOperator(token)) return Result.NO_MATCH;
                            LogUtils.debug("primitive operator: {}", token.getType());
                            operators.add((PrimitiveOperator) token.getType());

                            expectPrimitiveOperator = false;
                        } else {
                            Result matchResult = matchNextFor(TermSyntax::new, "term");

                            if (matchResult == Result.MATCHES_SO_FAR)
                                return Result.MATCHES_SO_FAR;
                            if (matchResult == Result.NO_MATCH) {
                                syntaxes.remove("term");
                                return Result.NO_MATCH;
                            }

                            TermSyntax term = (TermSyntax) syntaxes.get("term");
                            LogUtils.debug("found term: {}, terms before: {}", term, terms);
                            terms.add(term);
                            LogUtils.debug("found term: {}, terms after: {}", term, terms);

                            syntaxes.remove("term");
                            if (!isValidOperator(nextToken)) return Result.MATCHES_EXACT;

                            expectPrimitiveOperator = true;
                        }
                        return Result.MATCHES_SO_FAR;
                    }
                })
        ;

        LogUtils.warn("for {}, printed: \n{},\ndepth: {}",
                getClass().getName(),
                new SyntaxToString(this).represent(),
                rootNode.calculateDepth()
        );

    }

    @Override
    protected boolean isValidInStatus(Parser.Status status) {
        return true;
    }

    @Override
    protected boolean isValidInContext(DeclaredNode context) {
        return true;
    }

    @NotNull
    public OperationNode createOperationNode() {
        if (terms.size() == 0) throw new IllegalStateException("Cannot create operation node without terms");

        LogUtils.success("Found: \n{}, \n{}", terms, operators);
        if (terms.size() == 1) return terms.get(0).createOperationNode();

        BinaryOperationNode currentOpNode = new BinaryOperationNode(operators.get(0),
                terms.get(0).createOperationNode(),
                terms.get(1).createOperationNode());

        PeekingIterator<TermSyntax> termPeeker = PeekingIterator.of(terms);
        LogUtils.success("termPeeker has next after 1? {}, termPeeker: {}", termPeeker.hasNext(1), termPeeker.toList());
        termPeeker.advance(2);

        while (termPeeker.hasNext()) {
            PrimitiveOperator operator = operators.get(termPeeker.cursor());
            TermSyntax term = termPeeker.next();

            currentOpNode = new BinaryOperationNode(operator, currentOpNode, term.createOperationNode());
        }

        LogUtils.success("Found: \n{}", currentOpNode);
        return currentOpNode;
    }


    private boolean isValidOperator(Token candidate) {
        if (candidate == null) return false;
        if (!(candidate.getType() instanceof PrimitiveOperator)) return false;
        return validExpressionOperators.contains((PrimitiveOperator) candidate.getType());
    }

    @Override
    public String toString() {
        return "ExpressionSyntax{" +
                "inputs=" + getInputTokens() +
                '}';
    }
}
