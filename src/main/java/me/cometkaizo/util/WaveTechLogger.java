package me.cometkaizo.util;

import me.cometkaizo.logging.ConsoleColors;
import me.cometkaizo.logging.LogFormatter;
import me.cometkaizo.logging.Logger;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;

public class WaveTechLogger extends Logger {

    private final FatalLogFormatter fatal = new FatalLogFormatter();
    private final ErrorLogFormatter error = new ErrorLogFormatter();
    private final WarnLogFormatter warn = new WarnLogFormatter();
    private final InfoLogFormatter info = new InfoLogFormatter();
    private final DebugLogFormatter debug = new DebugLogFormatter();
    private final TraceLogFormatter trace = new TraceLogFormatter();

    private static final StackWalker STACK_WALKER = StackWalker.getInstance(StackWalker.Option.RETAIN_CLASS_REFERENCE);

    @Override
    public void fatal(String message, Object... args) {
        print(fatal.format(LocalDateTime.now(), STACK_WALKER.getCallerClass(), message, args));
    }

    @Override
    public void fatal(Object value) {
        fatal("{}", value);
    }

    @Override
    public void fatal(Throwable t, String message, Object... args) {
        fatal(message + ":", args);
        t.printStackTrace(err);
    }

    @Override
    public void fatal(Throwable t) {
        fatal(t, "A fatal error occurred");
    }

    @Override
    public void error(String message, Object... args) {
        err(error.format(LocalDateTime.now(), STACK_WALKER.getCallerClass(), message, args));
    }

    @Override
    public void error(Object value) {
        error("{}", value);
    }

    @Override
    public void report(Throwable t, String message, Object... args) {
        List<Object> argsList = new ArrayList<>(List.of(args));
        argsList.add(t);
        error(message + ":\n{}", argsList.toArray());
    }

    @Override
    public void report(Throwable t) {
        report(t, "An error occurred");
    }

    @Override
    public void warn(String message, Object... args) {
        print(warn.format(LocalDateTime.now(), STACK_WALKER.getCallerClass(), message, args));
    }

    @Override
    public void warn(Object value) {
        warn("{}", value);
    }

    @Override
    public void info(String message, Object... args) {
        print(info.format(LocalDateTime.now(), STACK_WALKER.getCallerClass(), message, args));
    }

    @Override
    public void info(Object value) {
        info("{}", value);
    }

    @Override
    public void debug(String message, Object... args) {
        print(debug.format(LocalDateTime.now(), STACK_WALKER.getCallerClass(), message, args));
    }

    @Override
    public void debug(Object value) {
        debug("{}", value);
    }

    @Override
    public void trace(String message, Object... args) {
        print(trace.format(LocalDateTime.now(), STACK_WALKER.getCallerClass(), message, args));
    }

    @Override
    public void trace(Object value) {
        trace("{}", value);
    }

    @Override
    public void log(LogFormatter formatter, String message, Object... args) {
        print(formatter.format(LocalDateTime.now(), STACK_WALKER.getCallerClass(), message, args));
    }

    @Override
    public void log(LogFormatter formatter, Object value) {
        log(formatter, "{}", value);
    }

    @Override
    public void r(String message, Object... args) {
        colored(ConsoleColors.RED, message, args);
    }

    @Override
    public void r(Object value) {
        r("{}", value);
    }

    @Override
    public void y(String message, Object... args) {
        colored(ConsoleColors.YELLOW, message, args);
    }

    @Override
    public void y(Object value) {
        y("{}", value);
    }

    @Override
    public void g(String message, Object... args) {
        colored(ConsoleColors.GREEN, message, args);
    }

    @Override
    public void g(Object value) {
        g("{}", value);
    }

    @Override
    public void b(String message, Object... args) {
        colored(ConsoleColors.CYAN, message, args);
    }

    @Override
    public void b(Object value) {
        b("{}", value);
    }

    @Override
    public void p(String message, Object... args) {
        colored(ConsoleColors.PURPLE, message, args);
    }

    @Override
    public void p(Object value) {
        p("{}", value);
    }

    @Override
    public void m(String message, Object... args) {
        colored(ConsoleColors.PURPLE_BRIGHT, message, args);
    }

    @Override
    public void m(Object value) {
        m("{}", value);
    }

    @Override
    public void colored(String colorCode, String message, Object... args) {
        print(debug.format(colorCode, LocalDateTime.now(), STACK_WALKER.getCallerClass(), message, args));
    }

    @Override
    public void colored(String colorCode, Object value) {
        colored(colorCode, "{}", value);
    }

    private void print(String string) {
        out.println(string);
    }

    private void err(String string) {
        err.println(string);
    }


    private static String shortenName(String name) {
        String[] parts = name.split("\\.");

        StringBuilder builder = new StringBuilder();

        for (int index = 0; index < parts.length - 1; index ++) {
            builder.append(parts[index], 0, 2);
            builder.append(".");
        }
        return builder.append(parts[parts.length - 1]).toString();
    }
    public static String withArgs(String message, Object... args) {
        if (args == null) {
            return message + " // WARNING: arguments were null";
        }
        String regex = "\\{}";
        for (Object arg : args) {
            if (arg == null) {
                message = message.replaceFirst(regex, "{NULL}");
                continue;
            }
            if (arg.getClass().isArray()) {
                message = message.replaceFirst(regex, Matcher.quoteReplacement(Arrays.deepToString((Object[]) arg)));
            } else if (List.class.isAssignableFrom(arg.getClass())) {
                message = message.replaceFirst(regex, Matcher.quoteReplacement(Arrays.deepToString(((List<?>) arg).toArray())));
            } else if (Map.class.isAssignableFrom(arg.getClass())) {
                message = message.replaceFirst(regex, Matcher.quoteReplacement(Arrays.deepToString(List.of(((Map<?, ?>) arg)).toArray())));
            } else {
                try {
                    message = message.replaceFirst(regex, Matcher.quoteReplacement(arg.toString()));
                } catch (IllegalArgumentException i) {
                    message = message.replaceFirst(regex, "{" + Matcher.quoteReplacement(arg.getClass().getSimpleName()) + " is not castable to String}");
                }
            }
        }
        return message;
    }

    static class FatalLogFormatter implements LogFormatter {
        public static final String format = "[&:&:&][&] //// FATAL> &";
        @Override
        public String format(LocalDateTime now, Class<?> caller, String message, Object... args) {
            return format
                    .replaceFirst(VARIABLE_CHAR, String.valueOf(now.getHour()))
                    .replaceFirst(VARIABLE_CHAR, String.valueOf(now.getMinute()))
                    .replaceFirst(VARIABLE_CHAR, String.valueOf(now.getSecond()))
                    .replaceFirst(VARIABLE_CHAR, shortenName(caller.getName()))
                    .replaceFirst(VARIABLE_CHAR, withArgs(message, args));
        }
    }

    static class ErrorLogFormatter implements LogFormatter {
        public static final String format = "[&:&:&][&] /// ERROR> &";
        @Override
        public String format(LocalDateTime now, Class<?> caller, String message, Object... args) {
            return format
                    .replaceFirst(VARIABLE_CHAR, String.valueOf(now.getHour()))
                    .replaceFirst(VARIABLE_CHAR, String.valueOf(now.getMinute()))
                    .replaceFirst(VARIABLE_CHAR, String.valueOf(now.getSecond()))
                    .replaceFirst(VARIABLE_CHAR, shortenName(caller.getName()))
                    .replaceFirst(VARIABLE_CHAR, withArgs(message, args));
        }
    }

    static class WarnLogFormatter implements LogFormatter {
        public static final String format = "[&:&:&][&] // WARN> &";
        @Override
        public String format(LocalDateTime now, Class<?> caller, String message, Object... args) {
            return format
                    .replaceFirst(VARIABLE_CHAR, String.valueOf(now.getHour()))
                    .replaceFirst(VARIABLE_CHAR, String.valueOf(now.getMinute()))
                    .replaceFirst(VARIABLE_CHAR, String.valueOf(now.getSecond()))
                    .replaceFirst(VARIABLE_CHAR, shortenName(caller.getName()))
                    .replaceFirst(VARIABLE_CHAR, withArgs(message, args));
        }
    }

    static class InfoLogFormatter implements LogFormatter {
        public static final String format = "[&:&:&][&] / INFO> &";
        @Override
        public String format(LocalDateTime now, Class<?> caller, String message, Object... args) {
            return format
                    .replaceFirst(VARIABLE_CHAR, String.valueOf(now.getHour()))
                    .replaceFirst(VARIABLE_CHAR, String.valueOf(now.getMinute()))
                    .replaceFirst(VARIABLE_CHAR, String.valueOf(now.getSecond()))
                    .replaceFirst(VARIABLE_CHAR, shortenName(caller.getName()))
                    .replaceFirst(VARIABLE_CHAR, withArgs(message, args));
        }
    }

    static class DebugLogFormatter implements LogFormatter {
        public static final String format = "&[&:&:&][&] / DEBUG>& &";

        public String format(String colorCode, LocalDateTime now, Class<?> caller, String message, Object... args) {
            return format
                    .replaceFirst(VARIABLE_CHAR, colorCode)
                    .replaceFirst(VARIABLE_CHAR, String.valueOf(now.getHour()))
                    .replaceFirst(VARIABLE_CHAR, String.valueOf(now.getMinute()))
                    .replaceFirst(VARIABLE_CHAR, String.valueOf(now.getSecond()))
                    .replaceFirst(VARIABLE_CHAR, shortenName(caller.getName()))
                    .replaceFirst(VARIABLE_CHAR, ConsoleColors.RESET)
                    .replaceFirst(VARIABLE_CHAR, withArgs(message, args));
        }

        @Override
        public String format(LocalDateTime now, Class<?> caller, String message, Object... args) {
            return format(ConsoleColors.BLUE, now, caller, message, args);
        }
    }

    static class TraceLogFormatter implements LogFormatter {
        public static final String format = "[&:&:&][&] / TRACE> &";
        @Override
        public String format(LocalDateTime now, Class<?> caller, String message, Object... args) {
            return format
                    .replaceFirst(VARIABLE_CHAR, String.valueOf(now.getHour()))
                    .replaceFirst(VARIABLE_CHAR, String.valueOf(now.getMinute()))
                    .replaceFirst(VARIABLE_CHAR, String.valueOf(now.getSecond()))
                    .replaceFirst(VARIABLE_CHAR, shortenName(caller.getName()))
                    .replaceFirst(VARIABLE_CHAR, withArgs(message, args));
        }
    }
}
