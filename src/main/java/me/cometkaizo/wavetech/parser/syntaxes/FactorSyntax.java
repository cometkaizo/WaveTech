package me.cometkaizo.wavetech.parser.syntaxes;

import me.cometkaizo.wavetech.parser.structures.Factor;
import me.cometkaizo.wavetech.syntaxes.Syntax;
import me.cometkaizo.wavetech.syntaxes.SyntaxStructure;
import me.cometkaizo.wavetech.syntaxes.SyntaxSyntaxNodeBuilder;
import org.jetbrains.annotations.NotNull;

public class FactorSyntax extends Syntax<Factor> {

    private FactorSyntax() {

        rootBuilder
                .split(
                        new SyntaxSyntaxNodeBuilder<>(LiteralSyntax::getInstance).withLabel("value"),
                        new SyntaxSyntaxNodeBuilder<>(MethodAccessSyntax::getNoSemicolonInstance).withLabel("resource"),
                        new SyntaxSyntaxNodeBuilder<>(VariableAccessSyntax::getInstance).withLabel("resource"),
                        new SyntaxSyntaxNodeBuilder<>(FieldAccessSyntax::getInstance).withLabel("resource")
                );

    }

    public static FactorSyntax getInstance() {
        return InstanceHolder.INSTANCE;
    }

    private static class InstanceHolder {
        static final FactorSyntax INSTANCE = new FactorSyntax();
    }

    @Override
    public @NotNull Factor getStructure(SyntaxStructure parent) {
        return new Factor();
    }
}
