package me.cometkaizo.util;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

@SuppressWarnings("unused")
public class RunLock {

    private final Predicate<Object> condition;
    private final StackTraceElement id;
    private static final List<StackTraceElement> ids = new ArrayList<>();

    public RunLock(Predicate<Object> condition) {
        this.condition = condition;
        this.id = Utils.getCallerTrace();
    }

    public RunLock() {
        this.condition = null;
        this.id = Utils.getCallerTrace();
    }

    public void run(Object subject, Runnable task) {
        synchronized (ids) {
            if (condition != null && !condition.test(subject)) {
                exit();
                return;
            }
            if (ids.contains(id)) {
                return;
            }
            ids.add(id);
            task.run();
        }
    }

    public boolean isLocked() {
        return ids.contains(id);
    }

    public void exit() {
        synchronized (ids) {
            ids.remove(id);
        }
    }

}
