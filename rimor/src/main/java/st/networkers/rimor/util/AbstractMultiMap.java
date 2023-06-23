package st.networkers.rimor.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public class AbstractMultiMap<K, V> extends MapAdapter<K, List<V>> implements MultiMap<K, V> {

    public AbstractMultiMap(Map<K, List<V>> delegate) {
        super(delegate);
    }

    @Override
    public boolean add(K key, V value) {
        return this.computeIfAbsent(key, k -> new ArrayList<>(1)).add(value);
    }

    @Override
    public boolean addAll(K key, Collection<V> values) {
        return this.computeIfAbsent(key, k -> new ArrayList<>(values.size())).addAll(values);
    }
}
