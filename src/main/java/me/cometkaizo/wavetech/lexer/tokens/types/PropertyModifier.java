package me.cometkaizo.wavetech.lexer.tokens.types;

import me.cometkaizo.wavetech.lexer.tokens.TokenType;

import java.util.ArrayList;
import java.util.List;

public enum PropertyModifier implements TokenType {

    STATIC("static"),
    FINAL("final"),
    ABSTRACT(new PropertyModifier[] {FINAL}, "abstract"),
    SEALED("sealed"),
    UNSEALED(new PropertyModifier[] {SEALED}, "unsealed"),
    VOLATILE(new PropertyModifier[] {FINAL}, "volatile"),
    TRANSIENT("transient"),
    NATIVE("native");

    private final String[] symbols;
    private final List<PropertyModifier> incompatibleModifiers;

    PropertyModifier(PropertyModifier[] incompatibleModifiers, String... symbols) {
        this.symbols = symbols;

        List<PropertyModifier> incompatibleModifiersList = new ArrayList<>(List.of(incompatibleModifiers));
        incompatibleModifiersList.add(this);
        this.incompatibleModifiers = incompatibleModifiersList;
    }
    PropertyModifier(String... symbols) {
        this.symbols = symbols;
        this.incompatibleModifiers = List.of();
    }

    @Override
    public String[] symbolSeq() {
        return symbols;
    }
    public boolean isCompatibleWith(PropertyModifier modifier) {
        return !incompatibleModifiers.contains(modifier);
    }
}
