package me.cometkaizo.wavetech.parser.structures;

import me.cometkaizo.wavetech.analyzer.structures.MethodContextAnalyzer;
import me.cometkaizo.wavetech.syntaxes.AbstractSyntaxStructure;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class IfStatement extends AbstractSyntaxStructure<MethodContextAnalyzer> implements MethodStatement {

    public Expression condition;
    public MethodBlock body;

    @Override
    public void store(String label, Object object) throws IllegalArgumentException {
        switch (label) {
            case "condition" -> condition = (Expression) object;
            case "body" -> body = (MethodBlock) object;
        }
    }

    @Override
    @NotNull
    protected MethodContextAnalyzer createAnalyzer() {
        return new IfStatementAnalyzer(this);
    }

    @Override
    public String toString() {
        return "IfStatement{" +
                "condition=" + condition +
                ", body=" + body +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        IfStatement that = (IfStatement) o;
        return Objects.equals(condition, that.condition) && Objects.equals(body, that.body);
    }

    @Override
    public int hashCode() {
        return Objects.hash(condition, body);
    }

}
