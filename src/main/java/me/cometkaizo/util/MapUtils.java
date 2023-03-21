package me.cometkaizo.util;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@SuppressWarnings("unused")
public class MapUtils {

    /**
     * credit: DrGodCarl
     */
    public static <K, V> Map<K, V> zipToMap(List<K> keys, List<V> values) {
        Iterator<K> keyIterator = keys.iterator();
        Iterator<V> valIterator = values.iterator();
        return IntStream.range(0, keys.size()).boxed()
                .collect(Collectors.toMap(_i -> keyIterator.next(), _i -> valIterator.next()));
    }

}
