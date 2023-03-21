package me.cometkaizo.wavetech.parser.structures;

import me.cometkaizo.wavetech.analyzer.ResourcePool;
import me.cometkaizo.wavetech.analyzer.diagnostics.Diagnostic;
import me.cometkaizo.wavetech.analyzer.structures.MethodAnalyzer;
import me.cometkaizo.wavetech.analyzer.structures.ProgramContextAnalyzer;
import me.cometkaizo.wavetech.lexer.tokens.Token;
import me.cometkaizo.wavetech.lexer.tokens.types.ModifierKeyword;
import me.cometkaizo.wavetech.lexer.tokens.types.VisibilityKeyword;
import me.cometkaizo.wavetech.syntaxes.AbstractSyntaxStructure;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class MethodStructure extends AbstractSyntaxStructure<MethodAnalyzer> implements EvaluableResource {

    public final ClassStructure parent;
    public VisibilityKeyword visibility = VisibilityKeyword.PACKAGE_PRIVATE;
    public Set<ModifierKeyword> modifiers = new HashSet<>(1);
    public ClassAccessor returnType;
    public Identifier name;
    public List<Parameter> parameters = new ArrayList<>(1);
    public MethodBlock body;


    public Signature signature;

    public Signature getSignature() {
        return signature;
    }

    @Override
    public @Nullable ClassStructure getTypeOrAnalyze(ResourcePool resources, List<Diagnostic> problems) {
        return (ClassStructure) resources.getResourceOrAnalyze(returnType, problems);
    }

    @Override
    public Identifier getTypeName() {
        return returnType != null ? returnType.className
                : null;
    }

    public boolean isStatic() {
        return modifiers.contains(ModifierKeyword.STATIC);
    }
    public boolean isAbstract() {
        return modifiers.contains(ModifierKeyword.ABSTRACT);
    }
    public boolean isFinal() {
        return modifiers.contains(ModifierKeyword.FINAL);
    }

    public MethodStructure(ClassStructure parent) {
        this.parent = parent;
    }

    @Override
    public void store(String label, Object object) {
        switch (label) {
            case "visibility" -> visibility = (VisibilityKeyword) ((Token) object).getType();
            case "modifier" -> modifiers.add((ModifierKeyword) ((Token) object).getType());
            case "returnType" -> returnType = (ClassAccessor) object;
            case "name" -> name = (Identifier) object;
            case "parameter" -> parameters.add((Parameter) object);
            case "body" -> body = (MethodBlock) object;
        }
    }

    @Override
    @NotNull
    protected MethodAnalyzer createAnalyzer() {
        return new MethodAnalyzer(this);
    }

    public static class Parameter extends AbstractSyntaxStructure<ProgramContextAnalyzer> implements EvaluableResource {

        public ClassAccessor typeAccessor;
        public Identifier name;

        @Override
        public void store(String label, Object object) {
            switch (label) {
                case "type" -> typeAccessor = (ClassAccessor) object;
                case "name" -> name = (Identifier) object;
            }
        }

        @Override
        public @Nullable ClassStructure getTypeOrAnalyze(ResourcePool resources, List<Diagnostic> problems) {
            return (ClassStructure) resources.getResourceOrAnalyze(typeAccessor, problems);
        }

        @Override
        public Identifier getTypeName() {
            return typeAccessor.className;
        }

        @Override
        public @NotNull ProgramContextAnalyzer createAnalyzer() {
            return new ProgramContextAnalyzer() {
                boolean analyzed = false;
                @Override
                public void analyze(List<Diagnostic> problems, ResourcePool resources) {
                    if (analyzed) return;
                    typeAccessor.getAnalyzer().analyze(problems, resources);
                    name.getAnalyzer().analyze(problems);
                    analyzed = true;
                }
            };
        }

        @Override
        public String toString() {
            return "Parameter{" +
                    "type=" + typeAccessor +
                    ", name=" + name +
                    '}';
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Parameter parameter = (Parameter) o;
            return Objects.equals(typeAccessor, parameter.typeAccessor) && Objects.equals(name, parameter.name);
        }

        @Override
        public int hashCode() {
            return Objects.hash(typeAccessor, name);
        }
    }

    public record Signature(Identifier name, List<ClassStructure> parameterTypes) {}

    @Override
    public String toString() {
        return "MethodStructure{" +
                "visibility=" + visibility +
                ", modifiers=" + modifiers +
                ", returnType=" + returnType +
                ", name=" + name +
                ", parameterTypes=" + parameters +
                ", body=\n" + String.valueOf(body).indent(4) +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MethodStructure that = (MethodStructure) o;
        return visibility == that.visibility && Objects.equals(modifiers, that.modifiers) && Objects.equals(returnType, that.returnType) && Objects.equals(name, that.name) && Objects.equals(parameters, that.parameters) && Objects.equals(body, that.body);
    }

    @Override
    public int hashCode() {
        return Objects.hash(visibility, modifiers, returnType, name, parameters, body);
    }
}
