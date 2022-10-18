package me.cometkaizo.system.driver;

import me.cometkaizo.system.app.App;
import me.cometkaizo.util.LogUtils;
import org.jetbrains.annotations.NotNull;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;


public abstract class SystemDriver {

    private static SystemDriver instance = null;

    private final App app;


    protected final ScheduledExecutorService executor = Executors.newScheduledThreadPool(2);
    protected List<ScheduledFuture<?>> driverLoops = new ArrayList<>(1);
    private boolean isRunning = false;

    protected SystemDriver(@NotNull App app) {
        instance = this;
        this.app = app;
    }

    public static InputStream getConsoleIn() {
        return System.in;
    }

    public static boolean currentDriverExists() {
        return instance != null;
    }
    public static SystemDriver getCurrentDriver() {
        if (!currentDriverExists()) throw new IllegalStateException("System has not created a driver yet");
        return instance;
    }
    public static Class<? extends SystemDriver> getCurrentDriverType() {
        return instance.getClass();
    }

    public void start() {
        if (isRunning) throw new IllegalStateException("Driver has already been started");

        setup();

        isRunning = true;
    }

    public void stop() {
        if (!isRunning) throw new IllegalStateException("Cannot stop a driver that is not running");

        LogUtils.debugCaller("Shutting down game...");

        cleanup();
        driverLoops.forEach(loop -> loop.cancel(false));

        isRunning = false;
    }

    protected void setup() {

    }

    protected void cleanup() {

    }

    public App getApp() {
        return app;
    }
}
