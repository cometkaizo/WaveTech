package me.cometkaizo.wavetech.parser.structures;

import me.cometkaizo.wavetech.analyzer.structures.ProgramContextAnalyzer;
import me.cometkaizo.wavetech.analyzer.structures.TernaryExpressionAnalyzer;
import me.cometkaizo.wavetech.syntaxes.AbstractSyntaxStructure;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class TernaryExpression extends AbstractSyntaxStructure<ProgramContextAnalyzer> {

    public LogicalOrExpression nextExpression;
    public Expression trueOption;
    public TernaryExpression falseOption;

    public ClassStructure type;

    @Override
    public void store(String label, Object object) throws IllegalArgumentException {
        switch (label) {
            case "nextExpression" -> nextExpression = cast(object);
            case "leftArg" -> trueOption = cast(object);
            case "rightArg" -> falseOption = cast(object);
        }
    }

    public @NotNull ProgramContextAnalyzer createAnalyzer() {
        return new TernaryExpressionAnalyzer(this);
    }

    @Override
    public String toString() {
        return "TernaryExpression{" +
                "nextExpression=" + nextExpression +
                ", trueOption=" + trueOption +
                ", falseOption=" + falseOption +
                ", type=" + type +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TernaryExpression that = (TernaryExpression) o;
        return Objects.equals(nextExpression, that.nextExpression) && Objects.equals(trueOption, that.trueOption) && Objects.equals(falseOption, that.falseOption) && Objects.equals(type, that.type);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nextExpression, trueOption, falseOption, type);
    }
}
