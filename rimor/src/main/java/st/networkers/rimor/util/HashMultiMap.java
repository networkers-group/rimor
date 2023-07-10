package st.networkers.rimor.util;

import java.util.*;

public class HashMultiMap<K, V> extends MapAdapter<K, List<V>> implements MultiMap<K, V> {

    public HashMultiMap() {
        super(new HashMap<>());
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