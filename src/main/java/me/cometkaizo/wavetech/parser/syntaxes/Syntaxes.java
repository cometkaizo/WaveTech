package me.cometkaizo.wavetech.parser.syntaxes;

import me.cometkaizo.wavetech.parser.syntaxes.nodes.Syntax;

import java.util.List;
import java.util.function.Supplier;

public final class Syntaxes {

    public static final List<Supplier<Syntax>> syntaxCreators = List.of(
            ClassDeclarationSyntax::new,
            FieldDeclarationSyntax::new,
            ClosingSymbolSyntax::new
    );

    private Syntaxes() {}

}
