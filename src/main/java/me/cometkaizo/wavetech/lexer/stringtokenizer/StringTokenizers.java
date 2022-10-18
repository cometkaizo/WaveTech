package me.cometkaizo.wavetech.lexer.stringtokenizer;

import java.util.List;

public final class StringTokenizers {

    // ORDERING MATTERS!!!!
    // earlier parsers may dominate later ones
    public static final List<StringTokenizer> tokenizers = List.of(
            new NullTokenizer(),
            new PrimitiveOperatorTokenizer(),
            new VisibilityModifierTokenizer(),
            new PropertyModifierTokenizer(),
            new DeclarationKeywordTokenizer(),
            new TypeKeywordTokenizer(),
            new BooleanTokenizer(),
            new DoubleTokenizer(),
            new IntegerTokenizer(), // int should be before double
            new SymbolTokenizer() // this is default case
    );

    private StringTokenizers() {}

}
