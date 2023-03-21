package me.cometkaizo.logging;

/**
 * package-private: cannot create new log-levels from outside
 */
public class LogLevel {

    private final String name;
    private final String color;
    private boolean enabled;

    LogLevel(String name, boolean enabled) {
        this.name = name;
        this.color = ConsoleColors.RESET;
        this.enabled = enabled;
    }
    LogLevel(String name, String color, boolean enabled) {
        this.name = name;
        this.color = color;
        this.enabled = enabled;
    }

    public String getName() {
        return name;
    }
    public String getColor() {
        return color;
    }

    public void setEnabled(boolean status) {
        enabled = status;
    }
    public boolean isEnabled() {
        return enabled;
    }

}
