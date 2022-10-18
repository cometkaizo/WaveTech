package me.cometkaizo.wavetech.lexer.tokens.types;

import java.util.HashMap;
import java.util.Map;

public class Keywords {


    private static final String numberRegex = "[0-9]";
    protected static final Map<Character, PrimitiveOperator> primitiveOperators = new HashMap<>();

    protected static final Map<String, VisibilityModifier> visibilityKeywords = new HashMap<>();
    protected static final Map<String, PropertyModifier> propertyModifiers = new HashMap<>();
    protected static final Map<String, DeclarationKeyword> declarationKeywords = new HashMap<>();
    protected static final Map<String, PrimitiveType> primitiveTypes = new HashMap<>();

    static {
        for (PrimitiveOperator modifier : PrimitiveOperator.values()) {
            primitiveOperators.put(modifier.getCharacter(), modifier);
        }
        for (VisibilityModifier modifier : VisibilityModifier.values()) {
            visibilityKeywords.put(modifier.getSymbol(), modifier);
        }
        for (PropertyModifier modifier : PropertyModifier.values()) {
            propertyModifiers.put(modifier.getSymbol(), modifier);
        }
        for (DeclarationKeyword keyword : DeclarationKeyword.values()) {
            declarationKeywords.put(keyword.getSymbol(), keyword);
        }
        for (PrimitiveType type : PrimitiveType.values()) {
            primitiveTypes.put(type.getSymbol(), type);
        }
    }


    public static boolean isNumber(char i) {
        return String.valueOf(i).matches(numberRegex);
    }

    public static boolean isPrimitiveOperator(char c) {
        return primitiveOperators.containsKey(c);
    }

    public static PrimitiveOperator parsePrimitiveOperator(char c) {
        return primitiveOperators.get(c);
    }

    public static boolean isValidSymbolName(String name) {
        return name.matches("[a-zA-Z$_]([a-zA-Z0-9$_]?)+");
    }

    public static boolean isVisibilityKeyword(String symbol) {
        return visibilityKeywords.containsKey(symbol);
    }
    public static VisibilityModifier parseVisibilityKeyword(String symbol) {
        return visibilityKeywords.get(symbol);
    }

    public static boolean isDeclarationKeyword(String symbol) {
        return declarationKeywords.containsKey(symbol);
    }
    public static DeclarationKeyword parseDeclarationKeyword(String symbol) {
        return declarationKeywords.get(symbol);
    }

    public static boolean isModifierKeyword(String symbol) {
        return propertyModifiers.containsKey(symbol);
    }
    public static PropertyModifier parseModifierKeyword(String symbol) {
        return propertyModifiers.get(symbol);
    }

    public static boolean isPrimitiveTypeKeyword(String symbol) {
        return primitiveTypes.containsKey(symbol);
    }
    public static PrimitiveType parsePrimitiveTypeKeyword(String symbol) {
        return primitiveTypes.get(symbol);
    }

}
