package me.cometkaizo.wavetech.parser.structures;

import me.cometkaizo.wavetech.analyzer.ResourcePool;
import me.cometkaizo.wavetech.analyzer.diagnostics.Diagnostic;
import me.cometkaizo.wavetech.analyzer.structures.FieldDeclarationAnalyzer;
import me.cometkaizo.wavetech.lexer.tokens.Token;
import me.cometkaizo.wavetech.lexer.tokens.types.ModifierKeyword;
import me.cometkaizo.wavetech.lexer.tokens.types.VisibilityKeyword;
import me.cometkaizo.wavetech.syntaxes.AbstractSyntaxStructure;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

public class FieldDeclaration extends AbstractSyntaxStructure<FieldDeclarationAnalyzer> implements EvaluableResource {

    public final ClassStructure parent;
    public ClassAccessor typeAccessor;
    public Identifier name;
    public VisibilityKeyword visibility = VisibilityKeyword.PACKAGE_PRIVATE;
    public Set<ModifierKeyword> modifiers = new HashSet<>(1);
    public AssignationExtension initializer;

    public FieldDeclaration(ClassStructure parent) {
        this.parent = parent;
    }

    @Override
    @NotNull
    protected FieldDeclarationAnalyzer createAnalyzer() {
        return new FieldDeclarationAnalyzer(this);
    }

    @Override
    public void store(String label, Object object) {
        switch (label) {
            case "name" -> name = cast(object);
            case "visibility" -> visibility = (VisibilityKeyword) ((Token) object).getType();
            case "modifier" -> modifiers.add((ModifierKeyword) ((Token) object).getType());
            case "type" -> typeAccessor = cast(object);
            case "initializer" -> initializer = cast(object);
        }
    }
/*
    public @NotNull List<ResourceAccessor<?>> getUsedFieldNames() {
        if (initializer.value != null) return initializer.value.getUsedResources();
        return List.of();
    }*/

    @Override
    public String toString() {
        return "FieldDeclarationStructure{" +
                "name=" + name +
                ", visibility=" + visibility +
                ", modifiers=" + modifiers +
                ", type=" + typeAccessor +
                ", initializer=" + initializer +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FieldDeclaration that = (FieldDeclaration) o;
        return Objects.equals(name, that.name) && visibility == that.visibility && Objects.equals(modifiers, that.modifiers) && Objects.equals(typeAccessor, that.typeAccessor) && Objects.equals(initializer, that.initializer);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, visibility, modifiers, typeAccessor, initializer);
    }

    public boolean isStatic() {
        return modifiers.contains(ModifierKeyword.STATIC);
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
