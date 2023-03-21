package me.cometkaizo.util;

import me.cometkaizo.logging.LogUtils;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

@SuppressWarnings("unused")
public final class Utils {

    public static void waitUntil(Supplier<Boolean> condition, int maxCheckAmount) {
        int checkAmount = 0;
        while (true) {
            if (checkAmount >= maxCheckAmount)
                return;
            if (condition.get())
                return;
            checkAmount ++;
        }
    }
    public static void waitUntil(Supplier<Boolean> condition, int samplePeriod, TimeUnit unit) {
        CountDownLatch latch = new CountDownLatch(1);

        try (ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor()) {
            latch.await();
            executor.scheduleAtFixedRate(() -> {
                if (condition.get())
                    latch.countDown();
            }, 0, samplePeriod, unit);
        } catch (InterruptedException e) {
            LogUtils.report(e, "Tried to use CountDownLatch when thread was interrupted");
        }
    }

    private Utils() {
        throw new AssertionError("No Utils instances for you!");
    }
}
