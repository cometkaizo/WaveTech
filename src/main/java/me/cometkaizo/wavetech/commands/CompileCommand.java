package me.cometkaizo.wavetech.commands;

import me.cometkaizo.commands.arguments.StringArgument;
import me.cometkaizo.commands.nodes.ArgumentCommandNodeBuilder;
import me.cometkaizo.commands.nodes.Command;
import me.cometkaizo.commands.CommandSyntaxException;
import me.cometkaizo.commands.nodes.EmptyCommandNodeBuilder;
import me.cometkaizo.logging.LogUtils;
import me.cometkaizo.wavetech.WaveTechApp;
import me.cometkaizo.wavetech.analyzer.Analyzer;
import me.cometkaizo.wavetech.analyzer.diagnostics.Diagnostic;
import me.cometkaizo.wavetech.lexer.Lexer;
import me.cometkaizo.wavetech.lexer.tokens.Token;
import me.cometkaizo.wavetech.parser.Parser;
import me.cometkaizo.wavetech.parser.diagnostics.ParseDiagnostic;

import java.io.File;
import java.util.List;
import java.util.stream.Collectors;

public class CompileCommand extends Command {
    public static final List<String> NAMES = List.of("compile");

    protected final WaveTechApp app;
    protected boolean verbose;
    protected boolean debug;

    public CompileCommand(WaveTechApp app) {
        this.app = app;
        rootNode
                .then(new ArgumentCommandNodeBuilder(new StringArgument("location")))
                .split(
                        new ArgumentCommandNodeBuilder(new StringArgument("flags")),
                        new EmptyCommandNodeBuilder()
                )
                .executes(this::configure)
                .executes(this::compile);
        resetConfig();
    }


    private void resetConfig() {
        verbose = false;
        debug = false;
    }

    private void compile() {
        useConfig();

        String location = (String) parsedArgs.get("location");
        File sourceFile = new File(location);

        LogUtils.debug("Compiling '{}'", location);

        // lexing
        Lexer lexer = new Lexer();
        List<Token> tokens = lexer.tokenize(sourceFile);
        LogUtils.success("Lexing Result: \n{}", tokens.stream().map(Object::toString).collect(Collectors.joining(",\n", "[\n", "\n]")));

        // parsing
        Parser parser = new Parser();
        Parser.Result parseResult = parser.parse(tokens);

        if (parseResult.isSuccessful()) {
            LogUtils.success("Parsing Result: \n{}", parseResult.compilationUnit());
        } else {
            var problems = parseResult.problems();
            LogUtils.error("Found {} syntax problem(s): \n{}",
                    problems.size(),
                    problems.stream().map(ParseDiagnostic::getDiagnostic).collect(Collectors.joining("\n", "[\n", "\n]")));
            return;
        }

        // analyzing
        Analyzer analyzer = new Analyzer(verbose);
        List<Diagnostic> problems = analyzer.analyze(parseResult.compilationUnit());

        if (problems.isEmpty()) LogUtils.success("No Semantic problems found!");
        else {
            LogUtils.error("Found {} Semantic problems(s):\n{}", problems.size(), problems.stream().map(Diagnostic::getDiagnostic).collect(Collectors.joining("\n", "[\n", "\n]")));
            return;
        }

        LogUtils.success("Compilation Success!");

    }

    private void useConfig() {
        if (debug) {
            LogUtils.enable(LogUtils.getSettings().INFO());
            LogUtils.enableDebug();
        } else {
            LogUtils.disable(LogUtils.getSettings().INFO());
            LogUtils.disableDebug();
        }
    }


    private void configure() {
        String flags = (String) parsedArgs.get("flags");
        if (flags == null) return;

        for (var flag : flags.split(":")) configure(flag);
    }

    private void configure(String flag) {
        switch (flag) {
            case "verbose" -> verbose = true;
            case "debug" -> debug = true;
            case "reset" -> resetConfig();
            default -> throw new CommandSyntaxException("Unknown flag '" + flag + "'");
        }
    }

    @Override
    public List<String> getNames() {
        return NAMES;
    }
}
