package me.cometkaizo.temp.syntaxes;

import me.cometkaizo.temp.syntaxes.nodes.Syntax;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public final class Syntaxes {

    // ORDER MATTERS!!!
    public static final List<Supplier<Syntax>> syntaxSuppliers = new ArrayList<>(List.of(
            ClassDeclarationSyntax::new,
            MethodDeclarationSyntax::new,
            FieldDeclarationSyntax::new,
            RightBraceSyntax::new,
            RightParenSyntax::new
    ));

    private Syntaxes() {}

    public static void register(Supplier<Syntax> syntaxSupplier) {
        syntaxSuppliers.add(syntaxSupplier);
    }
}
