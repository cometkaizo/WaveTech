package me.cometkaizo.util;

import me.cometkaizo.logging.LogUtils;
import org.jetbrains.annotations.Contract;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public final class StringUtils {


    /**
     * chops the string by the given regex, then returns the first section
     * @param s the string to chop
     * @param regex the regex to chop by
     * @return the first section of the chopped string
     */
    public static String chop(String s, String regex) {
        return s.split(regex)[0];
    }

    /**
     * chops the string by the given regex, then returns the section specified by the {@code index} <br>
     * to fetch the last element, you can pass -1. any number below that will correspond to the second last element,
     * third last, etc
     * @param s the string to chop
     * @param regex the regex to chop by
     * @param index the section to fetch
     * @throws ArrayIndexOutOfBoundsException if the specified index is larger than the amount of sections,
     * or, in the case of a negative number, that number is below the first element
     * @return the section specified by the {@code index}
     */
    public static String chop(String s, String regex, int index) {
        String[] elements = s.split(regex);
        if (index >= elements.length || elements.length + index < 0)
            throw new ArrayIndexOutOfBoundsException(LogUtils.withArgs("""
                    when separated by '{}', the string
                    '{}'
                    contained {} sections. index {} is out of bounds
                    original index: {}""",
                    regex,
                    s,
                    elements.length,
                    index >= 0 ? index : elements.length + index,
                    index
            ));
        if (index < 0)
            return elements[elements.length + index];
        return elements[index];
    }

    public static String collapseNewLines(String str) {
        return str.replaceAll("\n+", " ");
    }

    public static String collapseWhitespace(String str) {
        return str.replaceAll("\\s+", " ");
    }

    /**
     * How many times the target substring occurs in the string
     * @param str the string to examine
     * @param target the substring
     * @return how many times the target substring occurs in the string
     */
    public static int occurrencesOf(String str, String target) {
        if (target.equals("")) {
            if (str.equals(""))
                return 1;
            return 0;
        }
        return (str.length() - str.replace(target, "").length()) / target.length();
    }

    public static String max(String s1, String s2) {
        if (s1.length() > s2.length())
            return s1;
        return s2;
    }
    public static String min(String s1, String s2) {
        if (s1.length() < s2.length())
            return s1;
        return s2;
    }

    public static String splitLast(String str, String regex) {
        String[] split = str.split(regex);
        return split[split.length - 1];
    }

    public static boolean isBlank(String[] stringArray) {
        for (var string : stringArray) {
            if (string == null || !string.isBlank()) return false;
        }
        return true;
    }
    public static boolean isEmpty(String[] stringArray) {
        for (var string : stringArray) {
            if (string == null || !string.isEmpty()) return false;
        }
        return true;
    }


    @Contract(mutates = "param1")
    public static void trim(List<String> stringList) {
        int low = 0;
        int high = stringList.size();

        for (int index = 0; index < stringList.size(); index++) {
            String s = stringList.get(index);
            if (!s.isBlank()) {
                low = index;
                break;
            }
        }
        for (int index = stringList.size() - 1; index >= 0; index--) {
            String s = stringList.get(index);
            if (!s.isBlank()) {
                high = index + 1;
                break;
            }
        }

        stringList.subList(0, low).clear();
        stringList.subList(high, stringList.size()).clear();
    }

    public static String unicodeOf(char c) {
        return String.format("%04x", (int) c);
    }

    public static String join(List<String> strings) {
        StringBuilder builder = new StringBuilder();
        for (var string : strings) {
            builder.append(string);
        }
        return builder.toString();
    }
    public static String join(List<String> strings, String delimiter) {
        if (strings.isEmpty()) return "";

        StringBuilder builder = new StringBuilder(strings.get(0));
        for (int i = 1; i < strings.size(); i++) {
            String string = strings.get(i);

            builder.append(delimiter);
            builder.append(string);
        }
        return builder.toString();
    }

    /**
     * Acts exactly like {@link String#indent(int)} except does not add a newline at the end.
     * Returns an empty string for {@code null}.
     * @param string the string to indent
     * @param amt the indent amount, or amount to remove if negative
     * @return the indented string, or an empty string if the input was {@code null}
     * @see String#indent(int)
     */
    public static String indent(String string, int amt) {
        if (string == null) return "";
        if (string.isEmpty()) return "";
        if (amt == 0) return string;

        Stream<String> stream = string.lines();
        if (amt > 0) {
            var spaces = " ".repeat(amt);
            stream = stream.map(s -> spaces + s);
        } else if (amt == Integer.MIN_VALUE) {
            stream = stream.map(String::stripLeading);
        } else {
            stream = stream.map(s -> {
                char[] chars = s.toCharArray();
                for (int index = 0; index < chars.length; index++) {
                    int c = chars[index];
                    if (!Character.isWhitespace(c) || index == -amt) return s.substring(index);
                }
                return s;
            });
        }

        return stream.collect(Collectors.joining("\n"));
    }

    private StringUtils() {
        throw new AssertionError("No StringUtils instances for you!");
    }

}
