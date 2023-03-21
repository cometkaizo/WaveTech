package me.cometkaizo.wavetech.parser.structures;

import me.cometkaizo.wavetech.analyzer.structures.ClassAnalyzer;
import me.cometkaizo.wavetech.lexer.tokens.Token;
import me.cometkaizo.wavetech.lexer.tokens.types.ModifierKeyword;
import me.cometkaizo.wavetech.lexer.tokens.types.VisibilityKeyword;
import me.cometkaizo.wavetech.syntaxes.AbstractSyntaxStructure;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class ClassStructure extends AbstractSyntaxStructure<ClassAnalyzer> {

    public VisibilityKeyword visibility = VisibilityKeyword.PACKAGE_PRIVATE;
    public Set<ModifierKeyword> modifiers = new HashSet<>(1);
    public Identifier name;

    public List<FieldDeclaration> fields = new ArrayList<>(1);
    public List<MethodStructure> methods = new ArrayList<>(1);
    public List<FieldDeclaration> declaredFields = new ArrayList<>(1);
    public List<MethodStructure> declaredMethods = new ArrayList<>(1);

    @Override
    public void store(String label, Object object) throws IllegalArgumentException {
        switch (label) {
            case "visibility" -> visibility = (VisibilityKeyword) ((Token) object).getType();
            case "modifier" -> modifiers.add((ModifierKeyword) ((Token) object).getType());
            case "name" -> name = (Identifier) object;
            case "content" -> storeContent(object);
        }
    }

    private void storeContent(Object object) {
        if (object instanceof MethodStructure m) {
            declaredMethods.add(m);
            methods.add(m);
        }
        else if (object instanceof FieldDeclaration f) {
            declaredFields.add(f);
            fields.add(f);
        }
        else throw new IllegalArgumentException("Illegal element '" + object + "' in class body");
    }

    @Override
    @NotNull
    protected ClassAnalyzer createAnalyzer() {
        return new ClassAnalyzer(this);
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
    public boolean isSealed() {
        return modifiers.contains(ModifierKeyword.SEALED);
    }
    public boolean isUnsealed() {
        return modifiers.contains(ModifierKeyword.UNSEALED);
    }




    @Override
    public String toString() {
        return "ClassStructure{" +
                "visibility=" + visibility +
                ", modifiers=" + modifiers +
                ", name=" + name +
                ", \ndeclaredFields=" + declaredFields +
                ", \ndeclaredMethods=" + declaredMethods +
                "\n}";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ClassStructure that = (ClassStructure) o;
        return visibility == that.visibility && Objects.equals(modifiers, that.modifiers) && Objects.equals(name, that.name) && Objects.equals(declaredFields, that.declaredFields) && Objects.equals(declaredMethods, that.declaredMethods);
    }

    @Override
    public int hashCode() {
        return Objects.hash(visibility, modifiers, name, declaredFields, declaredMethods);
    }
}
