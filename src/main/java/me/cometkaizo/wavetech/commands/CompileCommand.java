package me.cometkaizo.wavetech.commands;

import me.cometkaizo.commands.arguments.StringArgument;
import me.cometkaizo.commands.nodes.ArgumentCommandNodeBuilder;
import me.cometkaizo.commands.nodes.Command;
import me.cometkaizo.util.LogUtils;
import me.cometkaizo.wavetech.WaveTechApp;
import me.cometkaizo.wavetech.lexer.Lexer;
import me.cometkaizo.wavetech.lexer.tokens.Token;
import me.cometkaizo.wavetech.parser.nodes.SourceFileNode;
import me.cometkaizo.wavetech.parser.Parser;

import java.io.File;
import java.util.List;
import java.util.stream.Collectors;

public class CompileCommand extends Command {

    protected final WaveTechApp app;

    public CompileCommand(WaveTechApp app) {
        this.app = app;
        rootNode
                .then(new ArgumentCommandNodeBuilder(new StringArgument("location")))
                .executes(this::compile);
    }

    private void compile() {
        String location = (String) parsedArgs.get("location");
        File sourceFile = new File(location);

        LogUtils.debug();

        Lexer lexer = new Lexer();
        List<Token> tokens = lexer.tokenize(sourceFile);
        LogUtils.success("Lexing Result: \n{}", tokens.stream().map(Object::toString).collect(Collectors.joining(",\n", "[\n", "\n]")));

        Parser parser = new Parser(tokens);
        SourceFileNode parseResult = parser.parse();
        LogUtils.success("Parsing Result: \n{}", parseResult);
    }
}
