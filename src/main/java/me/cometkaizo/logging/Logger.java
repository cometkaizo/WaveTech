package me.cometkaizo.logging;

import java.io.PrintStream;

public abstract class Logger {

    protected PrintStream out;
    protected PrintStream err;

    public Logger(PrintStream out, PrintStream err) {
        this.out = out;
        this.err = err;
    }

    public Logger() {
        this(System.out, System.err);
    }

    public abstract void fatal(String message, Object... args);

    public abstract void fatal(Object value);

    public abstract void fatal(Throwable t, String message, Object... args);

    public abstract void fatal(Throwable t);

    public abstract void error(String message, Object... args);

    public abstract void error(Object value);

    public abstract void report(Throwable t, String message, Object... args);

    public abstract void report(Throwable t);

    public abstract void warn(String message, Object... args);

    public abstract void warn(Object value);

    public abstract void info(String message, Object... args);

    public abstract void info(Object value);

    public abstract void debug(String message, Object... args);

    public abstract void debug(Object value);

    public abstract void trace(String message, Object... args);

    public abstract void trace(Object value);

    public abstract void log(LogFormatter formatter, String message, Object... args);

    public abstract void log(LogFormatter formatter, Object value);

    public abstract void r(String message, Object... args);
    public abstract void r(Object value);

    public abstract void y(String message, Object... args);
    public abstract void y(Object value);
    public abstract void g(String message, Object... args);
    public abstract void g(Object value);
    public abstract void b(String message, Object... args);
    public abstract void b(Object value);
    public abstract void p(String message, Object... args);
    public abstract void p(Object value);
    public abstract void m(String message, Object... args);
    public abstract void m(Object value);
    public abstract void colored(String colorCode, String message, Object... args);
    public abstract void colored(String colorCode, Object value);

    public PrintStream getOut() {
        return out;
    }

    public void setOut(PrintStream out) {
        this.out = out;
    }

    public PrintStream getErr() {
        return err;
    }

    public void setErr(PrintStream err) {
        this.err = err;
    }
}
