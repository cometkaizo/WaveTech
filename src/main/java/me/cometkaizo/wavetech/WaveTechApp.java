package me.cometkaizo.wavetech;

import me.cometkaizo.commands.CommandGroup;
import me.cometkaizo.commands.CommandSyntaxException;
import me.cometkaizo.commands.UnknownCommandException;
import me.cometkaizo.logging.LogUtils;
import me.cometkaizo.system.app.App;
import me.cometkaizo.wavetech.commands.CompileCommand;
import me.cometkaizo.wavetech.commands.ExecuteCommand;
import org.jetbrains.annotations.NotNull;

public class WaveTechApp extends App {

    private final CommandGroup commandGroup = new CommandGroup(
            () -> new CompileCommand(this),
            () -> new ExecuteCommand(this)
    );


    public void parseInput(@NotNull String input) {
        try {
            commandGroup.execute(input);
        } catch (CommandSyntaxException | UnknownCommandException e) {
            LogUtils.info(e.getMessage());
        }
    }


    @Override
    public WaveTechAppSettings getDefaultSettings() {
        return new WaveTechAppSettings();
    }

}
