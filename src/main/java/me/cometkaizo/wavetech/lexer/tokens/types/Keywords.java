package me.cometkaizo.wavetech.lexer.tokens.types;

import me.cometkaizo.wavetech.lexer.LineReader;
import org.jetbrains.annotations.Range;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import static me.cometkaizo.wavetech.lexer.tokens.types.PrimitiveOperator.*;

public final class Keywords {


    private static final Pattern numberRegex = Pattern.compile("[0-9]");
    private static final Pattern validNameRegex = Pattern.compile("[a-zA-Z$_][a-zA-Z0-9$_]*");
    public static final List<List<String>> primitiveOperatorSymbols;
    public static final List<PrimitiveOperator> primitiveOperators = new ArrayList<>();
    public static final List<PrimitiveOperator> unaryOperators = List.of(
            EXCLAMATION_MARK
    );
    private static final List<PrimitiveOperator> binaryOperators = List.of(
            PLUS,
            MINUS,
            ASTERISK,
            SLASH,
            PERCENT,
            CARET,
            AMPERSAND,
            DASTERISK,
            DAMPERSAND,
            PIPE,
            DOUBLE_PIPE,
            DOUBLE_EQUALS,
            GREATER_THAN,
            LESS_THAN,
            GREATER_THAN_OR_EQUAL,
            LESS_THAN_OR_EQUAL
    );

    public static final List<List<String>> visibilityModifierSymbols;
    public static final List<VisibilityModifier> visibilityModifiers = new ArrayList<>();
    public static final List<List<String>> propertyModifierSymbols;
    public static final List<PropertyModifier> propertyModifiers = new ArrayList<>();
    public static final List<List<String>> declarationKeywordSymbols;
    public static final List<DeclarationKeyword> declarationKeywords = new ArrayList<>();
    public static final List<List<String>> primitiveTypeSymbols;
    public static final List<PrimitiveType> primitiveTypes = new ArrayList<>();

    static {
        initializeLists();

        primitiveOperatorSymbols = primitiveOperators.stream()
                .map(o -> List.of(o.symbolSeq())).toList();
        visibilityModifierSymbols = visibilityModifiers.stream()
                .map(o -> List.of(o.symbolSeq())).toList();
        propertyModifierSymbols = propertyModifiers.stream()
                .map(o -> List.of(o.symbolSeq())).toList();
        declarationKeywordSymbols = declarationKeywords.stream()
                .map(o -> List.of(o.symbolSeq())).toList();
        primitiveTypeSymbols = primitiveTypes.stream()
                .map(o -> List.of(o.symbolSeq())).toList();
    }

    private static void initializeLists() {
        primitiveOperators.addAll(List.of(PrimitiveOperator.values()));
        visibilityModifiers.addAll(List.of(VisibilityModifier.values()));
        propertyModifiers.addAll(List.of(PropertyModifier.values()));
        declarationKeywords.addAll(List.of(DeclarationKeyword.values()));
        primitiveTypes.addAll(List.of(PrimitiveType.values()));


        primitiveOperators.sort(((o1, o2) -> Double.compare(o2.symbolSeq().length, o1.symbolSeq().length)));
        visibilityModifiers.sort(((o1, o2) -> Double.compare(o2.symbolSeq().length, o1.symbolSeq().length)));
        propertyModifiers.sort(((o1, o2) -> Double.compare(o2.symbolSeq().length, o1.symbolSeq().length)));
        declarationKeywords.sort(((o1, o2) -> Double.compare(o2.symbolSeq().length, o1.symbolSeq().length)));
        primitiveTypes.sort(((o1, o2) -> Double.compare(o2.symbolSeq().length, o1.symbolSeq().length)));

    }












    public static boolean isUnaryOperator(PrimitiveOperator operator) {
        if (operator == null) return false;
        return unaryOperators.contains(operator);
    }
    public static boolean isBinaryOperator(PrimitiveOperator operator) {
        if (operator == null) return false;
        return binaryOperators.contains(operator);
    }

    public static boolean isNumber(char i) {
        return numberRegex.matcher(String.valueOf(i)).matches();
    }

    public static boolean isValidSymbolName(String name) {
        return validNameRegex.matcher(name).matches();
    }





    public static boolean isAtPrimitiveOperator(LineReader reader) {
        return getValidSymbolIndexForReader(reader, primitiveOperatorSymbols) > -1;
    }

    public static PrimitiveOperator parsePrimitiveOperator(LineReader reader) {
        return primitiveOperators.get(getValidSymbolIndexForReaderAndAdvance(reader, primitiveOperatorSymbols));
    }

    public static boolean isAtVisibilityKeyword(LineReader reader) {
        return getValidSymbolIndexForReader(reader, visibilityModifierSymbols) > -1;
    }
    public static VisibilityModifier parseVisibilityKeyword(LineReader reader) {
        return visibilityModifiers.get(getValidSymbolIndexForReaderAndAdvance(reader, visibilityModifierSymbols));
    }

    public static boolean isAtDeclarationKeyword(LineReader reader) {
        return getValidSymbolIndexForReader(reader, declarationKeywordSymbols) > -1;
    }
    public static DeclarationKeyword parseDeclarationKeyword(LineReader reader) {
        return declarationKeywords.get(getValidSymbolIndexForReaderAndAdvance(reader, declarationKeywordSymbols));
    }

    public static boolean isAtModifierKeyword(LineReader reader) {
        return getValidSymbolIndexForReader(reader, propertyModifierSymbols) > -1;
    }
    public static PropertyModifier parseModifierKeyword(LineReader reader) {
        return propertyModifiers.get(getValidSymbolIndexForReaderAndAdvance(reader, propertyModifierSymbols));
    }

    public static boolean isAtPrimitiveTypeKeyword(LineReader reader) {
        return getValidSymbolIndexForReader(reader, primitiveTypeSymbols) > -1;
    }
    public static PrimitiveType parsePrimitiveTypeKeyword(LineReader reader) {
        return primitiveTypes.get(getValidSymbolIndexForReaderAndAdvance(reader, primitiveTypeSymbols));
    }

    @Range(from = -1, to = Integer.MAX_VALUE)
    private static int getValidSymbolIndexForReader(LineReader reader, List<List<String>> symbolList) {
        int symbolIndex = 0;
        for (var symbols : symbolList) {

            for (int peekAmt = 0; peekAmt < symbols.size(); peekAmt++) {
                if (!reader.hasNext(peekAmt - 1)) continue;

                String symbol = symbols.get(peekAmt);
                String actual = reader.peekWord(peekAmt);

                if (!symbol.equals(actual))
                    break;

                if (peekAmt == symbols.size() - 1)
                    return symbolIndex;
            }

            symbolIndex++;
        }
        return -1;
    }
    @Range(from = -1, to = Integer.MAX_VALUE)
    private static int getValidSymbolIndexForReaderAndAdvance(LineReader reader, List<List<String>> symbolList) {
        int symbolIndex = 0;
        for (var symbols : symbolList) {

            for (int peekAmt = 0; peekAmt < symbols.size(); peekAmt++) {
                if (!reader.hasNext(peekAmt - 1)) continue;

                String symbol = symbols.get(peekAmt);
                String actual = reader.peekWord(peekAmt);

                if (!symbol.equals(actual))
                    break;

                if (peekAmt == symbols.size() - 1) {
                    reader.advanceWord(peekAmt);
                    return symbolIndex;
                }
            }

            symbolIndex++;
        }
        return -1;
    }

    public static boolean isPrimitiveOperator(String candidate) {
        for (var symbols : primitiveOperatorSymbols) {
            if (symbols.size() != 1) continue;
            if (symbols.get(0).equals(candidate))
                return true;
        }
        return false;
    }


    private Keywords() {
        throw new AssertionError("No instances for you!");
    }
}
