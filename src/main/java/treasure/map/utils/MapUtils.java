package treasure.map.utils;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public final class MapUtils {

    private MapUtils() {}

    public static <I, K> Map<K, I> toMap(final I[] inputs, final Function<I, K> keyExtractor) {
        if (inputs == null) {
            return Collections.emptyMap();
        }

        Map<K, I> result = new HashMap<>(inputs.length);
        for (I input : inputs) {
            K key = keyExtractor.apply(input);

            result.put(key, input);
        }
        return result;
    }

    public static <I, K, V> Map<K, V> toMap(final Iterable<I> inputs, final Function<I, K> keyExtractor, final Function<I, V> valueExtractor) {
        if (inputs == null) {
            return Collections.emptyMap();
        }

        Map<K, V> result = new HashMap<>();
        for (I input : inputs) {
            K key = keyExtractor.apply(input);
            V value = valueExtractor.apply(input);

            result.put(key, value);
        }
        return result;
    }

}
