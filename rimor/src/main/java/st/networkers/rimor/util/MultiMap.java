package st.networkers.rimor.util;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;

public interface MultiMap<K, V> extends Map<K, List<V>> {

    boolean add(K key, V value);

    boolean addAll(K key, Collection<V> values);

    default boolean addIfAbsent(K key, V value) {
        if (this.get(key).contains(value))
            return false;

        return this.add(key, value);
    }

    default void ifPresent(K key, BiConsumer<K, List<V>> consumer) {
        List<V> values = this.get(key);
        if (values != null)
            consumer.accept(key, values);
    }
}
