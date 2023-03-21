package me.cometkaizo.wavetech.analyzer;

import me.cometkaizo.util.CollectionUtils;
import me.cometkaizo.wavetech.analyzer.diagnostics.Diagnostic;
import me.cometkaizo.wavetech.parser.structures.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MethodResourcePool implements ResourcePool {
    @NotNull
    public ClassResourcePool parent;
    @NotNull
    public List<MethodStructure.Parameter> parameters;
    @NotNull
    public List<VariableDeclaration> variables;

    public MethodResourcePool(
            @NotNull ClassResourcePool parent,
            @NotNull List<MethodStructure.Parameter> parameters,
            @NotNull List<VariableDeclaration> variables
    ) {
        this.parent = parent.copy();
        this.parameters = parameters;
        this.variables = variables;
    }

    public MethodResourcePool(MethodResourcePool other) {
        this(other.parent.copy(),
                new ArrayList<>(other.parameters),
                new ArrayList<>(other.variables));
    }

    @Override
    public boolean hasResourceOrAnalyze(ResourceAccessor<?> resourceAccessor, List<Diagnostic> problems) {
        if (resourceAccessor instanceof VariableAccessor variableAccessor) {
            if (hasParameter(variableAccessor.getSignature(), problems)) return true;
            if (hasVariable(variableAccessor.getSignature(), problems)) return true;
        }
        return parent.hasResourceOrAnalyze(resourceAccessor, problems);
    }

    @Override
    public @Nullable Object getResourceOrAnalyze(ResourceAccessor<?> resourceAccessor, List<Diagnostic> problems) {
        if (resourceAccessor instanceof VariableAccessor variableAccessor) {
            if (hasParameter(variableAccessor.getSignature(), problems))
                return getParameter(variableAccessor.getSignature(), problems);
            if (hasVariable(variableAccessor.getSignature(), problems))
                return getVariable(variableAccessor.getSignature(), problems);
        }
        return parent.getResourceOrAnalyze(resourceAccessor, problems);
    }

    @Override
    public MethodResourcePool copy() {
        return new MethodResourcePool(this);
    }

    public boolean hasParameter(Identifier identifier, List<Diagnostic> problems) {
        return CollectionUtils.anyMatch(parameters, p -> {
            p.getAnalyzer().analyze(problems, this);
            return Objects.equals(p.name, identifier);
        });
    }

    public MethodStructure.Parameter getParameter(Identifier identifier, List<Diagnostic> problems) {
        return CollectionUtils.getFirst(parameters, p -> {
            p.getAnalyzer().analyze(problems, this);
            return Objects.equals(p.name, identifier);
        }).orElse(null);
    }

    public boolean hasVariable(Identifier identifier, List<Diagnostic> problems) {
        return CollectionUtils.anyMatch(variables, v -> {
            v.getAnalyzer().analyze(problems, this);
            return Objects.equals(v.name, identifier);
        });
    }

    public VariableDeclaration getVariable(Identifier identifier, List<Diagnostic> problems) {
        return CollectionUtils.getFirst(variables, v -> {
            v.getAnalyzer().analyze(problems, this);
            return Objects.equals(v.name, identifier);
        }).orElse(null);
    }

    public boolean hasField(Identifier identifier, List<Diagnostic> problems) {
        return parent.hasField(identifier, problems);
    }

    public FieldDeclaration getField(Identifier identifier, List<Diagnostic> problems) {
        return parent.getField(identifier, problems);
    }

    public boolean hasMethod(MethodStructure.Signature signature, List<Diagnostic> problems) {
        return parent.hasMethod(signature, problems);
    }

    public MethodStructure getMethod(MethodStructure.Signature signature, List<Diagnostic> problems) {
        return parent.getMethod(signature, problems);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (MethodResourcePool) obj;
        return Objects.equals(this.parent, that.parent) &&
                Objects.equals(this.parameters, that.parameters) &&
                Objects.equals(this.variables, that.variables);
    }

    @Override
    public int hashCode() {
        return Objects.hash(parent, parameters, variables);
    }

    @Override
    public String toString() {
        return "MethodResourcePool[" +
                "classResourcePool=" + parent + ", " +
                "parameters=" + parameters + ", " +
                "variables=" + variables + ']';
    }

}
