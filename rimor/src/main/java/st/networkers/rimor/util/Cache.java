package st.networkers.rimor.util;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class Cache<K, V> extends MapAdapter<K, V> {

    private final Function<K, V> loader;

    public Cache(Function<K, V> loader) {
        this(new HashMap<>(), loader);
    }

    public Cache(Map<K, V> delegate, Function<K, V> loader) {
        super(delegate);
        this.loader = loader;
    }

    @Override
    @SuppressWarnings("unchecked")
    public V get(Object key) {
        return super.computeIfAbsent((K) key, loader);
    }
}
