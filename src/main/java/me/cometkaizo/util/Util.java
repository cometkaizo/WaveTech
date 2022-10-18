package me.cometkaizo.util;

import me.cometkaizo.exceptions.DeliberateUncloneableException;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.InvocationTargetException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

@SuppressWarnings("unused")
public abstract class Util {


    public static <T> @Nullable T cloneOrNull(T obj) {
        if (obj == null) {
            return null;
        }
        try {
            //noinspection unchecked
            return (T) obj.getClass().getMethod("clone").invoke(obj);
        } catch (InvocationTargetException e) {
            if (e.getCause().getClass().equals(CloneNotSupportedException.class)) {
                if (Cloneable.class.isAssignableFrom(obj.getClass())) {
                    LogUtils.error("Class '{}' cannot be cloned... skipping", obj.getClass());
                } else {
                    LogUtils.report(e.getCause(), "Class '{}' threw an exception while cloning",
                            obj.getClass());
                }
            } else if (e.getCause().getClass().equals(DeliberateUncloneableException.class)) {
                // swallow
                return obj;
            } else {
                LogUtils.report(e, "An invocation target exception occurred while cloning object of type '{}'",
                        obj.getClass());
            }
        } catch (IllegalAccessException e) {
            LogUtils.report(e, "Did not have access to clone method in class '{}'",
                    obj.getClass());
        } catch (NoSuchMethodException e) {
            LogUtils.report(e, "Could not find clone() method in class {}", obj.getClass());
        }
        return null;
    }
    @Contract("null -> null")
    public static <T> @Nullable T cloneOrException(T obj) {
        if (obj == null) {
            return null;
        }
        try {
            //noinspection unchecked
            return (T) obj.getClass().getMethod("clone").invoke(obj);
        } catch (InvocationTargetException e) {
            if (e.getCause().getClass().equals(CloneNotSupportedException.class)) {
                if (Cloneable.class.isAssignableFrom(obj.getClass())) {
                    throw new RuntimeException("Class '" + obj.getClass() + "' cannot be cloned");
                } else {
                    throw new RuntimeException("Class '" + obj.getClass() + "' threw an exception while cloning",
                            e.getCause());
                }
            } else if (e.getCause().getClass().equals(DeliberateUncloneableException.class)) {
                // swallow
                return obj;
            } else {
                throw new RuntimeException("An invocation target exception occurred while cloning object of type '" + obj.getClass() + "'", e);
            }
        } catch (IllegalAccessException e) {
            throw new RuntimeException("Did not have access to clone method in class '" + obj.getClass() + "'", e);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException("Could not find clone() method in class '" + obj.getClass() + "'", e);
        }
    }

    public static @Nullable StackTraceElement getCallerTrace() {
        StackTraceElement[] stElements = Thread.currentThread().getStackTrace();
        for (int i = 1; i < stElements.length; i ++) {
            StackTraceElement element = stElements[i];
            if (!element.getClassName().equals(Util.class.getName()) && element.getClassName().indexOf("java.lang.Thread") != 0) {
                return element;
            }
        }
        return null;
    }

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
}
