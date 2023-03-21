package me.cometkaizo.wavetech;

import me.cometkaizo.system.driver.SystemDriver;

import java.util.Scanner;
import java.util.concurrent.TimeUnit;

public class WaveTechDriver extends SystemDriver {

    private static final WaveTechApp app = new WaveTechApp();

    public WaveTechDriver() {
        super(app);
        addLoop(new Runnable() {
            final Scanner scanner = new Scanner(getConsoleIn());
            @Override
            public void run() {
                if (scanner.hasNextLine()) {
                    app.parseInput(scanner.nextLine());
                }
            }
        }, 300, TimeUnit.MILLISECONDS);
    }

}
