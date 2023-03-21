package me.cometkaizo.logging;

import me.cometkaizo.util.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Range;

import java.io.InputStream;
import java.io.PrintStream;
import java.time.LocalTime;
import java.util.*;
import java.util.regex.Matcher;

// TODO: 2022-10-28 Try using the new Logger.java (low priority)

public final class LogUtils {


    private static final Settings SETTINGS = new Settings(
            System.in,
            System.err,
            System.out,
            new LogLevel("SUCCESS", ConsoleColors.GREEN, true),
            new LogLevel("INFO", true),
            new LogLevel("WARN", ConsoleColors.YELLOW, true),
            new LogLevel("ERROR", ConsoleColors.RED, true),
            new LogLevel("FATAL", ConsoleColors.RED, true),
            new LogLevel("DEBUG", ConsoleColors.BLUE, true),
            new LogLevel("REQ-C", ConsoleColors.BLUE, true)
    );

    public record Settings(InputStream IN_STREAM, PrintStream ERR_STREAM, PrintStream OUT_STREAM, LogLevel SUCCESS, LogLevel INFO, LogLevel WARN, LogLevel ERROR, LogLevel FATAL, LogLevel DEBUG_0, LogLevel DEBUG_1) {
    }

    public static void enable(@NotNull LogLevel level) {
        level.setEnabled(true);
    }
    public static void enableAll() {
        SETTINGS.INFO.setEnabled(true);
        SETTINGS.WARN.setEnabled(true);
        SETTINGS.ERROR.setEnabled(true);
        SETTINGS.FATAL.setEnabled(true);
        SETTINGS.DEBUG_0.setEnabled(true);
        SETTINGS.DEBUG_1.setEnabled(true);
    }
    public static void disable(@NotNull LogLevel level) {
        level.setEnabled(false);
    }
    public static void disableAll() {
        SETTINGS.INFO.setEnabled(false);
        SETTINGS.WARN.setEnabled(false);
        SETTINGS.ERROR.setEnabled(false);
        SETTINGS.FATAL.setEnabled(false);
        SETTINGS.DEBUG_0.setEnabled(false);
        SETTINGS.DEBUG_1.setEnabled(false);
    }
    public static void enableDebug() {
        SETTINGS.DEBUG_0.setEnabled(true);
        SETTINGS.DEBUG_1.setEnabled(true);
    }
    public static void disableDebug() {
        SETTINGS.DEBUG_0.setEnabled(false);
        SETTINGS.DEBUG_1.setEnabled(false);
    }

    public static Settings getSettings() {
        return SETTINGS;
    }

    private static String getLogTime() {
        LocalTime now = LocalTime.now();
        return "[" +
                now.getMinute() + ":" +
                now.getSecond() + "]";
    }

    private static int getCallerLine() {
        return getCaller().map(StackTraceElement::getLineNumber).orElse(-1);
    }

    public static String shortenName(String name) {
        String[] parts = name.split("\\.");
        StringBuilder builder = new StringBuilder();
        for (int index = 0; index < parts.length - 1; index ++) {
            builder.append(parts[index], 0, 2);
            builder.append(".");
        }
        return builder.append(parts[parts.length - 1]).toString();
    }

    private static @NotNull Optional<StackTraceElement> getCaller() {
        StackTraceElement[] stackTraceElements = Thread.currentThread().getStackTrace();
        if (stackTraceElements.length == 0) {
            warn("""
                    The current thread does not have any stack trace elements!
                      the current thread has not started,\s
                      started but not scheduled to run yet,\s
                      or the thread has been terminated.""");
            return Optional.empty();
        }

        for (int index = 1; index < stackTraceElements.length; index ++) {
            if (!stackTraceElements[index].getClassName().equals(
                    LogUtils.class.getPackageName() + "." + LogUtils.class.getSimpleName()
            )) {
                return Optional.of(stackTraceElements[index]);
            }
        }

        SETTINGS.ERR_STREAM.println("LogUtil::getCaller(): could not find any element; stack trace size: " + stackTraceElements.length);
        return Optional.empty();
    }

    private static @NotNull Optional<StackTraceElement> getCallerCaller() {
        StackTraceElement[] stackTraceElements = Thread.currentThread().getStackTrace();
        if (stackTraceElements.length == 0) {
            warn("""
                    The current thread does not have any stack trace elements!
                      the current thread has not started,\s
                      started but not scheduled to run yet,\s
                      or the thread has been terminated.""");
            return Optional.empty();
        }

        boolean again = false;
        for (int index = 1; index < stackTraceElements.length; index ++) {
            if (again) {
                return Optional.of(stackTraceElements[index]);
            }
            again = !stackTraceElements[index].getClassName().equals(
                    LogUtils.class.getPackageName() + "." + LogUtils.class.getSimpleName()
            );
        }

        SETTINGS.ERR_STREAM.println("LogUtil::getCaller(): could not find any element; stack trace size: " + stackTraceElements.length);
        return Optional.empty();
    }

    public static @NotNull String build(Object... args) {
        if (args == null) {
            return "// WARNING: arguments were null";
        }
        StringBuilder builder = new StringBuilder();
        for (Object arg : args) {
            if (arg == null) {
                builder.append("{NULL ARGUMENT}");
                continue;
            }
            if (arg.getClass().isArray()) {
                builder.append(Arrays.deepToString((Object[]) arg));
            } else if (List.class.isAssignableFrom(arg.getClass())) {
                builder.append(Arrays.deepToString(((List<?>) arg).toArray()));
            } else if (Map.class.isAssignableFrom(arg.getClass())) {
                builder.append(Arrays.deepToString(List.of(((Map<?, ?>) arg)).toArray()));
            } else {
                try {
                    builder.append(Matcher.quoteReplacement(arg.toString()));
                } catch (IllegalArgumentException i) {
                    builder.append("{").append(Matcher.quoteReplacement(arg.getClass().getSimpleName())).append(" is not castable to String}");
                }
            }
        }
        return builder.toString();
    }

    /**
     * replaces '{}' in the given message with the arguments translated into strings.<br>
     * If a given argument cannot be translated to string, it will not throw an error, but simply put an error string instead
     * @param message the message to parse
     * @param args the args to put in the message
     * @return the message with all the arguments replacing '{}'
     */
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
                message = message.replaceFirst(regex, Matcher.quoteReplacement(arg.toString()));
            }
        }
        return message;
    }

    /**
     * prints an info message with optional arguments
     * @param message the message to be printed
     * @param args the arguments to be inserted into the message
     */
    public static void info(String message, Object... args) {
        if (SETTINGS.INFO.isEnabled()) {
            SETTINGS.OUT_STREAM.println(
                    build(SETTINGS.INFO.getColor(), getLogTime(), "[", shortenName(getCaller().map(StackTraceElement::getClassName).orElse("{NON-EXISTENT}")), ":", getCallerLine(), "]/",
                            SETTINGS.INFO.getName(), "> ", withArgs(message, args), ConsoleColors.RESET)
            );
        }
    }

    /**
     * prints a warning message with optional arguments
     * @param message the message to be printed
     * @param args the arguments to be inserted into the message
     */
    public static void warn(String message, Object... args) {
        if (SETTINGS.WARN.isEnabled()) {
            SETTINGS.OUT_STREAM.println(
                    build(SETTINGS.WARN.getColor(), getLogTime(), "[", shortenName(getCaller().map(StackTraceElement::getClassName).orElse("{NON-EXISTENT}")), ":", getCallerLine(), "] / "
                           , SETTINGS.WARN.getName(), "> ", withArgs(message, args), ConsoleColors.RESET)
            );
        }
    }

    /**
     * prints an error message with optional arguments
     * @param message the message to be printed
     * @param args the arguments to be inserted into the message
     */
    public static void error(String message, Object... args) {
        if (SETTINGS.ERROR.isEnabled()) {
            SETTINGS.ERR_STREAM.println(
                    build(SETTINGS.ERROR.getColor(), getLogTime(), "[", shortenName(getCaller().map(StackTraceElement::getClassName).orElse("{NON-EXISTENT}")), ":", getCallerLine(), "] // ",
                            SETTINGS.ERROR.getName(), "> ", withArgs(message, args), ConsoleColors.RESET)
            );
        }
    }

    /**
     * prints a fatal message with optional arguments, then attempts to stop the program
     * @param message the message to be printed
     * @param args the arguments to be inserted into the message
     */
    public static void fatal(String message, Object... args) {
        if (SETTINGS.FATAL.isEnabled()) {
            SETTINGS.ERR_STREAM.println(
                    build(SETTINGS.FATAL.getColor(), getLogTime(), "[", shortenName(getCaller().map(StackTraceElement::getClassName).orElse("{NON-EXISTENT}")), ":", getCallerLine(), "] /// ",
                            SETTINGS.FATAL.getName(), "> ", withArgs(message, args), ConsoleColors.RESET)
            );
        }
    }

    /**
     * prints an error message with optional arguments, then prints the stack trace of the error. <br>
     * this method is mainly purposed to simplify error reporting in catch blocks when you don't want to throw an error
     * but want to be notified when an error occurred
     * @param t the error to be reported
     * @param message the message to be printed
     * @param args the arguments to be inserted into the message
     */
    public static void fatal(@Nullable Throwable t, String message, Object... args) {

        if (SETTINGS.FATAL.isEnabled()) {
            SETTINGS.ERR_STREAM.println(
                    build(SETTINGS.FATAL.getColor(), getLogTime(), "[", shortenName(getCaller().map(StackTraceElement::getClassName).orElse("{NON-EXISTENT}")), ":", getCallerLine(), "] /// ",
                            SETTINGS.FATAL.getName(), "> ", withArgs(message + ": ", args), ConsoleColors.RESET)
            );
        }
        if (t != null) {
            t.printStackTrace(SETTINGS.ERR_STREAM);
        } else {
            error("{NULL EXCEPTION}");
        }

    }

    /**
     * prints an error message with optional arguments, then prints the stack trace of the error. <br>
     * this method is mainly purposed to simplify error reporting in catch blocks when you don't want to throw an error
     * but want to be notified when an error occurred
     * @param t the error to be reported
     * @param message the message to be printed
     * @param args the arguments to be inserted into the message
     */
    public static void report(@Nullable Throwable t, String message, Object... args) {

        error(message + ": ", args);
        if (t != null) {
            t.printStackTrace(SETTINGS.ERR_STREAM);
        } else {
            error("{NULL EXCEPTION}");
        }

    }

    /**
     * Prints the name of the method that ran this method and its class name.
     * This method is mainly purposed for use when debugging to see if a particular method is triggered or not
     */
    public static void debug() {
        if (!SETTINGS.DEBUG_0.isEnabled())
            return;
        getCaller().ifPresent(
            caller ->
            SETTINGS.OUT_STREAM.println(
                    build(SETTINGS.DEBUG_0.getColor(), getLogTime(), "[", shortenName(getCaller().map(StackTraceElement::getClassName).orElse("{NON-EXISTENT}")), ":", getCallerLine(), "]/"+ SETTINGS.DEBUG_0.getName(), "> ", ConsoleColors.RESET,
                            withArgs("{}::{} was invoked",
                                    StringUtils.chop(caller.getClassName(), "\\.", -1),
                                    caller.getMethodName()
                            )
                    )
            )
        );
    }
    /**
     * Prints the name of the method that ran this method, its class name, and additional info.
     * This method is mainly purposed for use when debugging to see if a certain section of a particular method is triggered or not
     * @param additionalInfo the additional information to add (if this is called multiple times
     *                       per method then this can be used to differentiate between the calls)
     * @param args the arguments that can be inserted into the info for a more informative debug
     */
    public static void debug(String additionalInfo, Object... args) {
        if (!SETTINGS.DEBUG_0.isEnabled())
            return;
        getCaller().ifPresent(
                caller ->
                        SETTINGS.OUT_STREAM.println(
                                build(SETTINGS.DEBUG_0.getColor(), getLogTime(), "[", shortenName(getCaller().map(StackTraceElement::getClassName).orElse("{NON-EXISTENT}")), ":", getCallerLine(), "]/"+ SETTINGS.DEBUG_0.getName(), "> ", ConsoleColors.RESET,
                                        withArgs("{}::{} -> {}",
                                                StringUtils.chop(caller.getClassName(), "\\.", -1),
                                                caller.getMethodName(),
                                                withArgs(additionalInfo, args)
                                        )
                                )
                        )
        );
    }

    /**
     * Prints the name of the method that ran this method, its class name, and the method that called it.
     * This method is mainly purposed for use when debugging to see if a particular method is triggered or not
     */
    public static void debugCaller() {
        if (!SETTINGS.DEBUG_1.isEnabled())
            return;
        getCaller().ifPresent(
                caller ->
                        getCallerCaller().ifPresent(
                                callerCaller ->
                                        SETTINGS.OUT_STREAM.println(
                                                build(SETTINGS.DEBUG_0.getColor(), getLogTime(), "[", shortenName(getCaller().map(StackTraceElement::getClassName).orElse("{NON-EXISTENT}")), ":", getCallerLine(), "]/"+ SETTINGS.DEBUG_0.getName(), "> ", ConsoleColors.RESET,
                                                        withArgs("{}::{} was invoked by {}::{}[{}]",
                                                                StringUtils.chop(caller.getClassName(), "\\.", -1),
                                                                caller.getMethodName(),
                                                                StringUtils.chop(callerCaller.getClassName(), "\\.", -1),
                                                                callerCaller.getMethodName(),
                                                                callerCaller.getLineNumber()
                                                        )
                                                )
                                        )
                        )
        );
    }
    /**
     * Prints the name of the method that ran this method, its class name, its caller method, and additional info.
     * This method is mainly purposed for use when debugging to see if a certain section of a particular method is triggered or not
     * @param additionalInfo the additional information to add (if this is called multiple times
     *                       per method then this can be used to differentiate between the calls)
     * @param args the arguments that can be inserted into the info for a more informative debug
     */
    public static void debugCaller(String additionalInfo, Object... args) {
        if (!SETTINGS.DEBUG_1.isEnabled())
            return;
        getCaller().ifPresent(
                caller ->
                        getCallerCaller().ifPresent(
                                callerCaller ->
                                        SETTINGS.OUT_STREAM.println(
                                                build(SETTINGS.DEBUG_0.getColor(), getLogTime(), "[", shortenName(getCaller().map(StackTraceElement::getClassName).orElse("{NON-EXISTENT}")), ":", getCallerLine(), "]/"+ SETTINGS.DEBUG_0.getName(), "> ", ConsoleColors.RESET,
                                                        withArgs("{}::{} was invoked by {}::{}[{}] || -> {}",
                                                                StringUtils.chop(caller.getClassName(), "\\.", -1),
                                                                caller.getMethodName(),
                                                                StringUtils.chop(callerCaller.getClassName(), "\\.", -1),
                                                                callerCaller.getMethodName(),
                                                                callerCaller.getLineNumber(),
                                                                withArgs(additionalInfo, args)
                                                        )
                                                )
                                        )
                        )
        );
    }


    public static void printStackTrace() {
        if (!SETTINGS.DEBUG_1.isEnabled())
            return;
        StackTraceElement[] stackTraceElements = Thread.currentThread().getStackTrace();
        if (stackTraceElements.length == 0) {
            warn("""
                    The current thread does not have any stack trace elements!
                      the current thread has not started,\s
                      started but not scheduled to run yet,\s
                      or the thread has been terminated.""");
            return;
        }

        StringBuilder r = new StringBuilder("complete stack trace elements:\n");
        for (StackTraceElement element : stackTraceElements) {
            r.append("\t")
                    .append(element.getClassName())
                    .append(".")
                    .append(element.getMethodName())
                    .append("(")
                    .append(StringUtils.chop(element.getClassName(), "\\.", -1))
                    .append(".java")
                    .append(":")
                    .append(element.getLineNumber())
                    .append(")\n");
        }
        SETTINGS.OUT_STREAM.println(
                build(SETTINGS.DEBUG_1.getColor(), getLogTime(), "[", shortenName(getCaller().map(StackTraceElement::getClassName).orElse("{NON-EXISTENT}")), ":", getCallerLine(), "]/", SETTINGS.DEBUG_1.getName(), "> ", r, ConsoleColors.RESET));
    }
    public static void printStackTrace(@Range(from = 1, to = Integer.MAX_VALUE) int length) {
        if (!SETTINGS.DEBUG_1.isEnabled())
            return;
        StackTraceElement[] stackTraceElements = Thread.currentThread().getStackTrace();
        if (stackTraceElements.length == 0) {
            warn("""
                    The current thread does not have any stack trace elements!
                      the current thread has not started,\s
                      started but not scheduled to run yet,\s
                      or the thread has been terminated.""");
            return;
        }

        StringBuilder r = new StringBuilder(length + " most recent method calls:\n");
        for (int i = 0; i < length; i++) {
            StackTraceElement element = stackTraceElements[i];
            r.append("\t")
                    .append(element.getClassName())
                    .append(".")
                    .append(element.getMethodName())
                    .append("(")
                    .append(StringUtils.chop(element.getClassName(), "\\.", -1))
                    .append(".java")
                    .append(":")
                    .append(element.getLineNumber())
                    .append(")\n");
        }
        SETTINGS.OUT_STREAM.println(
                build(SETTINGS.DEBUG_1.getColor(), getLogTime(), "[", shortenName(getCaller().map(StackTraceElement::getClassName).orElse("{NON-EXISTENT}")), ":", getCallerLine(), "]/", SETTINGS.DEBUG_1.getName(), "> ", r, ConsoleColors.RESET));
    }

    public static String prompt(String prompt) {
        Scanner scanner = new Scanner(SETTINGS.IN_STREAM);
        info(prompt);
        String result = scanner.nextLine();
        scanner.close();
        return result;
    }
    public static String prompt(String prompt, String... expected) {
        Scanner scanner = new Scanner(SETTINGS.IN_STREAM);
        List<String> expectedResult = List.of(expected);
        while (true) {
            info(prompt);
            if (scanner.hasNextLine()) {
                String result = scanner.nextLine();
                if (expectedResult.contains(result)) {
                    scanner.close();
                    return result;
                }
            } else {
                LogUtils.error("Could not find next line. returned null");
                return null;
            }
        }
    }

    public static void success(String message, Object... args) {
        if (SETTINGS.SUCCESS.isEnabled()) {
            SETTINGS.OUT_STREAM.println(
                    build(SETTINGS.SUCCESS.getColor(), getLogTime(), "[", shortenName(getCaller().map(StackTraceElement::getClassName).orElse("{NON-EXISTENT}")), ":", getCallerLine(), "]/",
                            SETTINGS.SUCCESS.getName(), "> ", withArgs(message, args), ConsoleColors.RESET)
            );
        }
    }



    private LogUtils() {
        throw new AssertionError("No LogUtils instances for you!");
    }

}
