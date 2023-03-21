package me.cometkaizo.commands;

import java.util.List;

public class UnknownCommandException extends RuntimeException {
    public UnknownCommandException(String commandName, List<List<String>> names) {
        super("Command '" + commandName + "' is not in this command group: \n" + names);
    }

}
