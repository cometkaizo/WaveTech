package me.cometkaizo.wavetech.parser.structures;

import me.cometkaizo.wavetech.analyzer.ResourcePool;
import me.cometkaizo.wavetech.analyzer.diagnostics.Diagnostic;
import me.cometkaizo.wavetech.analyzer.structures.MethodContextAnalyzer;
import me.cometkaizo.wavetech.analyzer.structures.VariableDeclarationAnalyzer;
import me.cometkaizo.wavetech.lexer.tokens.Token;
import me.cometkaizo.wavetech.lexer.tokens.types.ModifierKeyword;
import me.cometkaizo.wavetech.syntaxes.AbstractSyntaxStructure;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

public class VariableDeclaration extends AbstractSyntaxStructure<MethodContextAnalyzer> implements MethodStatement, EvaluableResource {

    public ClassAccessor typeAccessor;
    public Identifier name;
    public Set<ModifierKeyword> modifiers = new HashSet<>(1);
    public AssignationExtension initializer;

    @Override
    public void store(String label, Object object) {
        switch (label) {
            case "name" -> name = cast(object);
            case "modifier" -> modifiers.add((ModifierKeyword) ((Token) object).getType());
            case "type" -> typeAccessor = cast(object);
            case "initializer" -> initializer = cast(object);
        }
    }

    @Override
    @NotNull
    protected MethodContextAnalyzer createAnalyzer() {
        return new VariableDeclarationAnalyzer(this);
    }

    @Override
    public String toString() {
        return "VariableDeclarationStatement{" +
                "name=" + name +
                ", modifiers=" + modifiers +
                ", type=" + typeAccessor +
                ", initializer=" + initializer +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        VariableDeclaration that = (VariableDeclaration) o;
        return Objects.equals(name, that.name) && Objects.equals(modifiers, that.modifiers) && Objects.equals(typeAccessor, that.typeAccessor) && Objects.equals(initializer, that.initializer);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, modifiers, typeAccessor, initializer);
    }

    @Override
    public @Nullable ClassStructure getTypeOrAnalyze(ResourcePool resources, List<Diagnostic> problems) {
        return (ClassStructure) resources.getResourceOrAnalyze(typeAccessor, problems);
    }

    @Override
    public Identifier getTypeName() {
        return typeAccessor.className;
    }

}
