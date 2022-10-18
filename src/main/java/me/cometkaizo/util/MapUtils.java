package me.cometkaizo.util;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@SuppressWarnings("unused")
public class MapUtils {

    /**
     * Deep copies the map, assuming all elements inside are cloneable
     * @param original the map to be deep copied
     * @param <K> the map key type
     * @param <V> the map value type
     * @return the deep copied map
     */
    public static <K, V> Map<K, V> deepCopy(Map<? extends K, ? extends V> original) {
        List<K> deepCopiedKeys = ArrayUtils.deepCopy(new ArrayList<>(original.keySet()));
        List<V> deepCopiedValues = ArrayUtils.deepCopy(new ArrayList<>(original.values()));
        return zipToMap(deepCopiedKeys, deepCopiedValues);
    }

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
