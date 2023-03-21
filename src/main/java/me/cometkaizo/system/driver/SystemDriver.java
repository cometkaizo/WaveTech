package me.cometkaizo.system.driver;

import me.cometkaizo.logging.LogUtils;
import me.cometkaizo.system.app.App;
import org.jetbrains.annotations.NotNull;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;


public abstract class SystemDriver {

    private final App app;


    private final ScheduledExecutorService executor = Executors.newScheduledThreadPool(2);
    private final List<ScheduledFuture<?>> tasks = new ArrayList<>(1);
    private boolean isRunning = false;

    protected SystemDriver(@NotNull App app) {
        this.app = app;
    }

    public static InputStream getConsoleIn() {
        return System.in;
    }

    public void start() {
        if (isRunning) throw new IllegalStateException("Driver has already been started");

        setup();

        isRunning = true;
    }

    public void stop() {
        if (!isRunning) throw new IllegalStateException("Driver is not started");

        cleanup();
        tasks.forEach(loop -> loop.cancel(false));
        tasks.clear();

        isRunning = false;
    }

    protected void setup() {

    }

    protected void cleanup() {

    }


    protected final void addLoop(Runnable task, long period, TimeUnit unit, ExceptionManager exceptionManager) {
        addTask(executor.scheduleAtFixedRate(() -> {
            try {
                task.run();
            } catch (Exception e) {
                Throwable newEx = exceptionManager.handleException(e);
                if (newEx != null) throw newEx instanceof RuntimeException r ? r : new RuntimeException(newEx);
            } catch (Error err) {
                Throwable newEx = exceptionManager.handleError(err);
                if (newEx != null) throw newEx instanceof RuntimeException r ? r : new RuntimeException(newEx);
                throw err;
            }
        }, 0, period, unit));
    }

    protected final void addLoop(Runnable task, long period, TimeUnit unit) {
        addLoop(task, period, unit, new ExceptionManager() {
            @Override
            public Throwable handleException(Exception e) {
                LogUtils.report(e, "Encountered exception");
                return null;
            }

            @Override
            public Error handleError(Error err) {
                LogUtils.report(err, "Encountered fatal exception");
                return err;
            }
        });
    }

    protected final void addTask(ScheduledFuture<?> task) {
        tasks.add(task);
    }

    public App getApp() {
        return app;
    }
}
