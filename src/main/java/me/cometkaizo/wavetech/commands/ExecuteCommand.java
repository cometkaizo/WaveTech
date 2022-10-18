package me.cometkaizo.wavetech.commands;

import me.cometkaizo.commands.arguments.StringArgument;
import me.cometkaizo.commands.nodes.ArgumentCommandNodeBuilder;
import me.cometkaizo.commands.nodes.Command;
import me.cometkaizo.wavetech.WaveTechApp;

public class ExecuteCommand extends Command {

    protected final WaveTechApp app;

    public ExecuteCommand(WaveTechApp app) {
        this.app = app;

        rootNode
                .then(new ArgumentCommandNodeBuilder(new StringArgument("location")))
                .executes(this::run);

    }

    private void run() {/*
        WaveSourceCode sourceCode = app.getCompiledSourceFiles().get((String) parsedArgs.get("location"));

        if (sourceCode == null) {
            LogUtils.error("Source code location '{}' cannot be found", parsedArgs.get("location"));
            return;
        }

        sourceCode.execute();*/
    }


}
