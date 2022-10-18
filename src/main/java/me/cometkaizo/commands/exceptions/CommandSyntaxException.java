package me.cometkaizo.commands.exceptions;

public class CommandSyntaxException extends RuntimeException {
    public CommandSyntaxException(String message) {
        super(message);
    }
}
