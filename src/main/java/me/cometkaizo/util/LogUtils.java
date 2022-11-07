package me.cometkaizo.util;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Range;

import java.io.InputStream;
import java.io.PrintStream;
import java.time.LocalDateTime;
import java.util.*;
import java.util.regex.Matcher;

// TODO: 2022-10-28 Try using the new Logger.java (low priority)

public abstract class LogUtils {
    
    
    private static PrintStream out = System.out;
    private static PrintStream err = System.err;
    private static InputStream in = System.in;

    private static final LogLevel SUCCESS = new LogLevel("SUCCESS", ConsoleColors.GREEN, true);
    private static final LogLevel INFO = new LogLevel("INFO", true);
    private static final LogLevel WARN = new LogLevel("WARN", ConsoleColors.YELLOW, true);
    private static final LogLevel ERROR = new LogLevel("ERROR", ConsoleColors.RED, true);
    private static final LogLevel FATAL = new LogLevel("FATAL", ConsoleColors.RED, true);
    private static final LogLevel IMPOSSIBLE = new LogLevel("IMPOSSIBLE", ConsoleColors.RED, true);
    private static final LogLevel DEBUG_0 = new LogLevel("RUN", ConsoleColors.BLUE, true);
    private static final LogLevel DEBUG_1 = new LogLevel("REQ-C", ConsoleColors.BLUE, true);

    public static void enable(@NotNull LogLevel level) {
        level.setEnabled(true);
    }
    public static void enableAll() {
        INFO.setEnabled(true);
        WARN.setEnabled(true);
        ERROR.setEnabled(true);
        FATAL.setEnabled(true);
        IMPOSSIBLE.setEnabled(true);
        DEBUG_0.setEnabled(true);
        DEBUG_1.setEnabled(true);
    }
    public static void disable(@NotNull LogLevel level) {
        level.setEnabled(false);
    }
    public static void disableAll() {
        INFO.setEnabled(false);
        WARN.setEnabled(false);
        ERROR.setEnabled(false);
        FATAL.setEnabled(false);
        IMPOSSIBLE.setEnabled(false);
        DEBUG_0.setEnabled(false);
        DEBUG_1.setEnabled(false);
    }
    public static void enableDebug() {
        DEBUG_0.setEnabled(true);
        DEBUG_1.setEnabled(true);
    }
    public static void disableDebug() {
        DEBUG_0.setEnabled(false);
        DEBUG_1.setEnabled(false);
    }
    
    public static void setPrintStream(PrintStream out) {
        LogUtils.out = out;
    }
    public static void setErrorStream(PrintStream err) {
        LogUtils.err = err;
    }
    public static void setInputStream(InputStream in) {
        LogUtils.in = in;
    }
    public static PrintStream getPrintStream() {
        return out;
    }
    public static PrintStream getErrorStream() {
        return err;
    }
    public static InputStream getInputStream() {
        return in;
    }

    private static String getLogTime() {
        LocalDateTime now = LocalDateTime.now();
        return "[" + now.getHour() + ":" +
                now.getMinute() + ":" +
                now.getSecond() + ":" +
                "//]";
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

        err.println("LogUtil::getCaller(): could not find any element; stack trace size: " + stackTraceElements.length);
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

        err.println("LogUtil::getCaller(): could not find any element; stack trace size: " + stackTraceElements.length);
        return Optional.empty();
    }

    public static @NotNull String build(Object... args) {
        if (args == null) {
            return "// WARNING: arguments were null.";
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
            return message + " // WARNING: arguments were null.";
        }
        String regex = "\\{}";
        for (Object arg : args) {
            if (arg == null) {
                message = message.replaceFirst(regex, "{ARGUMENT WAS NULL}");
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

    /**
     * prints an info message with optional arguments
     * @param message the message to be printed
     * @param args the arguments to be inserted into the message
     */
    public static void info(String message, Object... args) {
        if (INFO.isEnabled()) {
            out.println(
                    build(INFO.getColor(), getLogTime(), "[", shortenName(getCaller().map(StackTraceElement::getClassName).orElse("{NON-EXISTENT}")), ":", getCallerLine(), "]/",
                            INFO.getName(), "> ", withArgs(message, args), ConsoleColors.RESET)
            );
        }
    }

    /**
     * prints a warning message with optional arguments
     * @param message the message to be printed
     * @param args the arguments to be inserted into the message
     */
    public static void warn(String message, Object... args) {
        if (WARN.isEnabled()) {
            out.println(
                    build(WARN.getColor(), getLogTime(), "[", shortenName(getCaller().map(StackTraceElement::getClassName).orElse("{NON-EXISTENT}")), ":", getCallerLine(), "] / "
                           , WARN.getName(), "> ", withArgs(message, args), ConsoleColors.RESET)
            );
        }
    }

    /**
     * prints an error message with optional arguments
     * @param message the message to be printed
     * @param args the arguments to be inserted into the message
     */
    public static void error(String message, Object... args) {
        if (ERROR.isEnabled()) {
            err.println(
                    build(ERROR.getColor(), getLogTime(), "[", shortenName(getCaller().map(StackTraceElement::getClassName).orElse("{NON-EXISTENT}")), ":", getCallerLine(), "] // ",
                            ERROR.getName(), "> ", withArgs(message, args), ConsoleColors.RESET)
            );
        }
    }

    /**
     * prints a fatal message with optional arguments, then attempts to stop the program
     * @param message the message to be printed
     * @param args the arguments to be inserted into the message
     */
    public static void fatal(String message, Object... args) {
        if (FATAL.isEnabled()) {
            err.println(
                    build(FATAL.getColor(), getLogTime(), "[", shortenName(getCaller().map(StackTraceElement::getClassName).orElse("{NON-EXISTENT}")), ":", getCallerLine(), "] /// ",
                            FATAL.getName(), "> ", withArgs(message, args), ConsoleColors.RESET)
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

        if (FATAL.isEnabled()) {
            err.println(
                    build(FATAL.getColor(), getLogTime(), "[", shortenName(getCaller().map(StackTraceElement::getClassName).orElse("{NON-EXISTENT}")), ":", getCallerLine(), "] /// ",
                            FATAL.getName(), "> ", withArgs(message + ": ", args), ConsoleColors.RESET)
            );
        }
        if (t != null) {
            t.printStackTrace(err);
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
            t.printStackTrace(err);
        } else {
            error("{NULL EXCEPTION}");
        }

    }

    /**
     * Prints the name of the method that ran this method and its class name.
     * This method is mainly purposed for use when debugging to see if a particular method is triggered or not
     */
    public static void debug() {
        if (!DEBUG_0.isEnabled())
            return;
        getCaller().ifPresent(
            caller ->
            out.println(
                    build(DEBUG_0.getColor(), getLogTime(), "[", shortenName(getCaller().map(StackTraceElement::getClassName).orElse("{NON-EXISTENT}")), ":", getCallerLine(), "]/"+ DEBUG_0.getName(), "> ", ConsoleColors.RESET,
                            withArgs("{}.{}() was invoked",
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
        if (!DEBUG_0.isEnabled())
            return;
        getCaller().ifPresent(
                caller ->
                        out.println(
                                build(DEBUG_0.getColor(), getLogTime(), "[", shortenName(getCaller().map(StackTraceElement::getClassName).orElse("{NON-EXISTENT}")), ":", getCallerLine(), "]/"+ DEBUG_0.getName(), "> ", ConsoleColors.RESET,
                                        withArgs("{}.{}() -> {}",
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
        if (!DEBUG_1.isEnabled())
            return;
        getCaller().ifPresent(
                caller ->
                        getCallerCaller().ifPresent(
                                callerCaller ->
                                        out.println(
                                                build(DEBUG_0.getColor(), getLogTime(), "[", shortenName(getCaller().map(StackTraceElement::getClassName).orElse("{NON-EXISTENT}")), ":", getCallerLine(), "]/"+ DEBUG_0.getName(), "> ", ConsoleColors.RESET,
                                                        withArgs("{}.{}() was invoked by {}.{}()[{}]",
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
        if (!DEBUG_1.isEnabled())
            return;
        getCaller().ifPresent(
                caller ->
                        getCallerCaller().ifPresent(
                                callerCaller ->
                                        out.println(
                                                build(DEBUG_0.getColor(), getLogTime(), "[", shortenName(getCaller().map(StackTraceElement::getClassName).orElse("{NON-EXISTENT}")), ":", getCallerLine(), "]/"+ DEBUG_0.getName(), "> ", ConsoleColors.RESET,
                                                        withArgs("{}.{}() was invoked by {}.{}()[{}] || -> {}",
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
        if (!DEBUG_1.isEnabled())
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
        out.println(
                build(DEBUG_1.getColor(), getLogTime(), "[", shortenName(getCaller().map(StackTraceElement::getClassName).orElse("{NON-EXISTENT}")), ":", getCallerLine(), "]/", DEBUG_1.getName(), "> ", r, ConsoleColors.RESET));
    }
    public static void printStackTrace(@Range(from = 1, to = Integer.MAX_VALUE) int length) {
        if (!DEBUG_1.isEnabled())
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
        out.println(
                build(DEBUG_1.getColor(), getLogTime(), "[", shortenName(getCaller().map(StackTraceElement::getClassName).orElse("{NON-EXISTENT}")), ":", getCallerLine(), "]/", DEBUG_1.getName(), "> ", r, ConsoleColors.RESET));
    }

    public static String prompt(String prompt) {
        Scanner scanner = new Scanner(in);
        info(prompt);
        String result = scanner.nextLine();
        scanner.close();
        return result;
    }
    public static String prompt(String prompt, String... expected) {
        Scanner scanner = new Scanner(in);
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
        if (SUCCESS.isEnabled()) {
            out.println(
                    build(SUCCESS.getColor(), getLogTime(), "[", shortenName(getCaller().map(StackTraceElement::getClassName).orElse("{NON-EXISTENT}")), ":", getCallerLine(), "]/",
                            SUCCESS.getName(), "> ", withArgs(message, args), ConsoleColors.RESET)
            );
        }
    }

}
