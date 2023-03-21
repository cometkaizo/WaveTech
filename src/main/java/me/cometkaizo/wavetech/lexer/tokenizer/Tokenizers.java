package me.cometkaizo.wavetech.lexer.tokenizer;

import java.util.List;

public final class Tokenizers {

    // ORDERING MATTERS!!!!
    // earlier parsers may dominate later ones
    public static final List<Tokenizer> tokenizers = List.of(
            new NullTokenizer(),
            new PrimitiveOperatorTokenizer(),
            new VisibilityModifierTokenizer(),
            new PropertyModifierTokenizer(),
            new DeclarationKeywordTokenizer(),
            new PrimitiveTypeTokenizer(),
            new FunctionalKeywordTokenizer(),
            new BooleanTokenizer(),
            new DoubleTokenizer(),
            new IntegerTokenizer(),
            new SymbolTokenizer() // this is kind of default case
    );

    private Tokenizers() {}

}
