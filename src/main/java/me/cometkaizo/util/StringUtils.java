package me.cometkaizo.util;

public abstract class StringUtils {


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
        return str.replaceAll("\n", " ");
    }

    public static String collapseWhitespace(String str) {
        return str.replaceAll("\\s", " ");
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
}
