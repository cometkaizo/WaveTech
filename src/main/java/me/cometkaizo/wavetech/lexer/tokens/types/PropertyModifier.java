package me.cometkaizo.wavetech.lexer.tokens.types;

import me.cometkaizo.wavetech.lexer.tokens.TokenType;

import java.util.ArrayList;
import java.util.List;

public enum PropertyModifier implements TokenType {

    STATIC("static"),
    FINAL("final"),
    ABSTRACT("abstract", FINAL),
    SEALED("sealed"),
    UNSEALED("unsealed", SEALED),
    VOLATILE("volatile", FINAL),
    TRANSIENT("transient"),
    NATIVE("native");

    private final String symbol;
    private final List<PropertyModifier> incompatibleModifiers;

    PropertyModifier(String symbol, PropertyModifier... incompatibleModifiers) {
        this.symbol = symbol;

        List<PropertyModifier> incompatibleModifiersList = new ArrayList<>(List.of(incompatibleModifiers));
        incompatibleModifiersList.add(this);
        this.incompatibleModifiers = incompatibleModifiersList;
    }
    PropertyModifier(String symbol) {
        this.symbol = symbol;
        this.incompatibleModifiers = List.of();
    }

    public String getSymbol() {
        return symbol;
    }
    public boolean isCompatibleWith(PropertyModifier modifier) {
        return !incompatibleModifiers.contains(modifier);
    }
}
