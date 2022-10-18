package me.cometkaizo.wavetech;

import me.cometkaizo.system.driver.SystemDriver;
import me.cometkaizo.util.LogUtils;

import java.util.Scanner;
import java.util.concurrent.TimeUnit;

public class WaveTechDriver extends SystemDriver {

    private static final WaveTechApp app = new WaveTechApp();

    public WaveTechDriver() {
        super(app);
    }

    @Override
    public void start() {
        super.start();
        driverLoops.add(executor.scheduleAtFixedRate(new Runnable() {
            final Scanner scanner = new Scanner(SystemDriver.getConsoleIn());
            @Override
            public void run() {
                if (scanner.hasNextLine()) {
                    try {
                        app.parseInput(scanner.nextLine());
                    } catch (Exception e) {
                        LogUtils.report(e, "");
                    }
                }
            }
        }, 0, 300, TimeUnit.MILLISECONDS));
    }
}
