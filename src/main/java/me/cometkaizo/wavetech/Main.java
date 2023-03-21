package me.cometkaizo.wavetech;

import me.cometkaizo.logging.LogUtils;

public class Main {
    public static void main(String[] args) {

        LogUtils.info("Hello World! test: compile src/main/resources/testsource/hello_world.txt debug");

        WaveTechDriver driver = new WaveTechDriver();
        driver.start();

    }
}