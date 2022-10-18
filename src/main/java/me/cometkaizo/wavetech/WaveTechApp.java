package me.cometkaizo.wavetech;

import me.cometkaizo.system.app.App;
import me.cometkaizo.util.LogUtils;
import me.cometkaizo.wavetech.commands.CompileCommand;
import me.cometkaizo.wavetech.commands.ExecuteCommand;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Objects;

public class WaveTechApp extends App {

    public void parseInput(@NotNull String input) {
        Objects.requireNonNull(input, "Input cannot be null");
        if (input.isBlank()) return;

        String[] parts = input.split(" ");
        if (parts.length == 0) return;

        String command = parts[0];
        String[] args = Arrays.copyOfRange(parts, 1, parts.length);

        LogUtils.info("hi");

        switch (command) {
            case "exit" -> System.exit(0);
            case "compile" -> new CompileCommand(this).execute(args);
            case "execute" -> new ExecuteCommand(this).execute(args);
            default -> LogUtils.info("'{}' is not a command", command);
        }
    }


    @Override
    public WaveTechAppSettings getDefaultSettings() {
        return new WaveTechAppSettings();
    }

}
