package me.cometkaizo.wavetech;

import me.cometkaizo.util.LogUtils;

public class Main {
    public static void main(String[] args) {

        LogUtils.info("Hello World! test: compile src/main/resources/testsource/hello_world.txt");

        WaveTechDriver driver = new WaveTechDriver();
        driver.start();

    }
}