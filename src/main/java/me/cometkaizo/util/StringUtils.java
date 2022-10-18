package me.cometkaizo.util;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;

@SuppressWarnings("unused")
public abstract class StringUtils {

    public static String readFile(File file, Charset encoding) throws IOException {
        byte[] encoded = Files.readAllBytes(file.toPath());
        return new String(encoded, encoding);
    }
    public static String readFile(File file) throws IOException {
        return readFile(file, Charset.defaultCharset());
    }

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

    public static String collapseNewLines(String s, String replacement) {
        return s.replaceAll("\n", replacement);
    } public static String collapseNewLines(String s) {
        return s.replaceAll("\n", "");
    }

    public static String collapseWhitespace(String s) {
        s = replaceAllSequences(s, "\s", " ");
        s = replaceAllSequences(s, "\n\r", " ");
        s = replaceAllSequences(s, "\n", " ");
        s = replaceAllSequences(s, "\r", " ");
        s = replaceAllSequences(s, "\t", "  ");
        return s;
    }
    public static String collapseWhitespace(String str, String s, String nr, String n, String r, String t) {
        str = replaceAllSequences(str, "\s", s);
        str = replaceAllSequences(str, "\n\r", nr);
        str = replaceAllSequences(str, "\n", n);
        str = replaceAllSequences(str, "\r", r);
        str = replaceAllSequences(str, "\t", t);
        return str;
    }

    /**
     * Replaces all sequences of a regex with a replacement. <br>
     * Note this is different from {@link String#replaceAll(String, String)}:
     * <blockquote><font color="gray">
     *     String str = <font color="green">"aaaac"</font>;<br>
     *     str.replaceAll(<font color="green">"a"</font>, <font color="green">"b"</font>);
     * </blockquote></font>
     *would return <font color="green">"bbbbc"</font>. However,
     * <blockquote><font color="gray">
     *     str.replaceAllSequences(<font color="green">"a"</font>, <font color="green">"b"</font>);
     * </blockquote></font>
     * would return <font color="green">"bc"</font>
     * @param s the string to replace in
     * @param regex the string to replace
     * @param replacement the replacement string
     * @return the string, but with all sequences replaced
     */
    public static String replaceAllSequences(String s, String regex, String replacement) {
        while (occurrencesOf(s, regex + regex) > 0) {
            //LogUtil.ran("s: {}, occurrences: {}", s, occurrencesOf(s, regex + regex));
            s = s.replaceAll(regex + regex, regex);
        }
        //LogUtil.ran("s: {}, occurrences: {}", s, occurrencesOf(s, regex + regex));
        return s.replaceAll(regex, replacement);
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
/*
    /**
     * Rules: <br>
     * <blockquote>
     * null, null -> ["", ""] <br>
     * <i>param1</i>, null -> [<i>param1</i>, ""] <br>
     * null, <i>param2</i> -> ["", <i>param2</i>] <br>
     * "", "" -> ["", ""] <br>
     * "a", "ab" -> ["", "b"] <br>
     * "ab", "a" -> ["b", ""] <br>
     * "abc", "ac" -> ["b", ""] <br>
     * "abc", "av" -> ["bc", "v"] <br>
     * </blockquote>
     * @param s1 the first string
     * @param s2 the second string
     * @return the difference between the two strings
     *./
    public static String[] difference(String s1, String s2) {
        String[] result = {"", ""};
        if (s2 == null)
            result[0] = s1;
        if (s1 == null)
            result[1] = s2;
        else if (s1.equals(s2))
            return result;

        for (int index = 0; index <; index++) {

        }


        return result;
    }*/

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

}
