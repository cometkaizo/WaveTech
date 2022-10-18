package me.cometkaizo.wavetech.parser.syntaxparseres;

import java.util.List;
import java.util.function.Supplier;

public final class SyntaxParsers {

    public static final List<Supplier<SyntaxParser>> syntaxMatcherCreators = List.of(
            ClassDeclarationSyntaxParser::new,
            FieldDeclarationSyntaxParser::new,
            ClosingSymbolSyntaxParser::new
    );

    private SyntaxParsers() {}

}
