package me.cometkaizo.system.app;

public abstract class App {

    private final AppSettings settings;

    protected App() {
        this.settings = getDefaultSettings();
    }


    public abstract AppSettings getDefaultSettings();

    public void setup() {

    }

    public void cleanup() {

    }


    public AppSettings getSettings() {
        return settings;
    }

}
