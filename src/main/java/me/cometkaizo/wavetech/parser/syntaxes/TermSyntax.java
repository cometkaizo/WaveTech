package me.cometkaizo.wavetech.parser.syntaxes;

import me.cometkaizo.wavetech.lexer.tokens.types.OperatorKeyword;
import me.cometkaizo.wavetech.parser.structures.Term;
import me.cometkaizo.wavetech.syntaxes.Syntax;
import me.cometkaizo.wavetech.syntaxes.SyntaxStructure;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import static me.cometkaizo.wavetech.lexer.tokens.types.OperatorKeyword.*;

public class TermSyntax extends Syntax<Term> {

    protected List<FactorSyntax> factors = new ArrayList<>();
    protected List<OperatorKeyword> operators = new ArrayList<>();

    private TermSyntax() {

        rootBuilder
                .then("factor", FactorSyntax::getInstance)
                .zeroOrMore(ExtraFactorSyntax::new);

    }

    public static TermSyntax getInstance() {
        return InstanceHolder.INSTANCE;
    }

    private static class InstanceHolder {
        static final TermSyntax INSTANCE = new TermSyntax();
    }

    public static class ExtraFactorSyntax extends Syntax<Term> {
        public ExtraFactorSyntax() {
            rootBuilder
                    .split("operator",
                            ASTERISK,
                            SLASH,
                            PERCENT,
                            DOUBLE_ASTERISK,
                            DOUBLE_EQUALS,
                            LESS_THAN,
                            LESS_THAN_OR_EQUAL,
                            GREATER_THAN,
                            GREATER_THAN_OR_EQUAL
                    )
                    .then("factor", FactorSyntax::getInstance)
            ;
        }

        @Override
        public @NotNull Term getStructure(SyntaxStructure parent) {
            return (Term) parent;
        }

    }

    @Override
    public String toString() {
        return "TermSyntax{" +
                "factors=" + factors +
                ", operators=" + operators +
                '}';
    }

    @Override
    public @NotNull Term getStructure(SyntaxStructure parent) {
        return new Term();
    }
}
