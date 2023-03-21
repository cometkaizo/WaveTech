package me.cometkaizo.wavetech.parser.syntaxes;

import me.cometkaizo.logging.LogUtils;
import me.cometkaizo.wavetech.parser.structures.CompilationUnit;
import me.cometkaizo.wavetech.syntaxes.Syntax;
import me.cometkaizo.wavetech.syntaxes.SyntaxPrinter;
import me.cometkaizo.wavetech.syntaxes.SyntaxStructure;
import org.jetbrains.annotations.NotNull;

public class CompilationUnitSyntax extends Syntax<CompilationUnit> {

    private CompilationUnitSyntax() {
        rootBuilder
                .anchorStart()
                .then("class", ClassDeclarationSyntax::getInstance)
                .anchorEnd()
        ;

        LogUtils.info("Compilation Unit: \n{}", new SyntaxPrinter().calculateString(this));
    }

    public static CompilationUnitSyntax getInstance() {
        return InstanceHolder.INSTANCE;
    }

    private static class InstanceHolder{
        static final CompilationUnitSyntax INSTANCE = new CompilationUnitSyntax();
    }

    @Override
    public @NotNull CompilationUnit getStructure(SyntaxStructure parent) {
        return new CompilationUnit();
    }
}
