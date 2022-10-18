package me.cometkaizo.util;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.function.Predicate;

@SuppressWarnings("unused")
public class ArrayUtils {

    /**
     * Deep copies the list, assuming all elements inside are cloneable
     * @param original the list to be deep copied
     * @param <E> the list type
     * @return the deep copied list
     */
    public static <E> List<E> deepCopy(List<? extends E> original) {
        List<E> deepCopy = new ArrayList<>(0);
        for (E element : original) {
            deepCopy.add(Util.cloneOrNull(element));
        }
        return deepCopy;
    }

    public static <E> E getStrict(List<E> list, int index) {
        return list.get(index);
    } public static <E, F> F getStrict(Map<E, F> map, E key) {
        F result = map.get(key);
        if (result == null)
            throw new IllegalArgumentException(LogUtils.withArgs(
                    "there was no mapping to key '{}'",
                    key));
        return result;
    }

    public static <E> E filterFirst(List<E> list, Predicate<? super E> predicate) {
        //LogUtils.ran("checkpoint 3.211 - ran filterFirst");
        return filterSingle(list, predicate, 0);
    }
    public static <E> E filterSingle(List<E> l, Predicate<? super E> predicate, int index) {
        //LogUtils.ran("checkpoint 3.2111 - ran filterSingle");
        List<E> list = l.stream().filter(predicate).toList();
        if (index >= list.size() || list.size() + index < 0) {
            throw new NoSuchElementException(LogUtils.withArgs("""
                            List elements that match predicate: {}
                            index: {}
                            index {} is out of bounds""",
                    list.size(),
                    index,
                    index >= 0 ? index : list.size() + index
            ));
        }
        if (index < 0) {
            //LogUtils.ran("checkpoint 3.2112 - returned from filterFirst < 0");
            return list.get(list.size() + index);
        }
        //LogUtils.ran("checkpoint 3.2112 - returned from filterFirst >= 0");
        return list.get(index);

    }

    @Contract("_ -> param1")
    public static <T> T[] reverse(T[] array) {
        for(int i = 0; i < array.length / 2; i++) {
            T temp = array[i];
            array[i] = array[array.length - i - 1];
            array[array.length - i - 1] = temp;
        }
        return array;
    }
    @Contract("_ -> param1")
    public static <T> ArrayList<T> reverse(ArrayList<T> array) {
        for(int i = 0; i < array.size() / 2; i++) {
            T temp = array.get(i);
            array.set(i, array.get(array.size() - i - 1));
            array.set(array.size() - i - 1, temp);
        }
        return array;
    }

    public static <T> @NotNull T[] requireNoNullElement(@NotNull T[] list, String message) {
        if (Arrays.asList(list).contains(null)) {
            throw new NullPointerException(message);
        }
        return list;
    }

    /**
     * Aims to output a string that could be used in a compiler.
     * toStringWithBraces(new int[] {0, 5, 2, 3}) would output "{0, 5, 2, 3}"
     * @param arr the array to be formatted
     * @return a string representation of the array with braces
     */
    public static String toStringWithBraces(Object[] arr) {
        StringBuilder result = new StringBuilder("{");
        for (int i = 0; i < arr.length; i++) {
            if (i != 0)
                result.append(", ");
            result.append(arr[i].toString());
        }
        return result.append("}").toString();
    }

}
