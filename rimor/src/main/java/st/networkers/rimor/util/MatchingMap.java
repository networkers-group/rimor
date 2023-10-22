package st.networkers.rimor.util;

import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.AbstractMap.SimpleEntry;
import java.util.Map.Entry;

public class MatchingMap<K extends MatchingKey, V> {

    private final Map<Integer, List<Entry<K, V>>> delegate;

    public MatchingMap() {
        this.delegate = new HashMap<>();
    }

    public MatchingMap(MatchingMap<K, V> m) {
        this(new HashMap<>(m.getDelegate()));
    }

    public MatchingMap(Map<Integer, List<Entry<K, V>>> delegate) {
        this.delegate = delegate;
    }

    public Map<Integer, List<Entry<K, V>>> getDelegate() {
        return delegate;
    }

    public boolean isEmpty() {
        return delegate.isEmpty();
    }

    @Nullable
    public V put(K key, V value) {
        List<Entry<K, V>> entries = delegate.computeIfAbsent(key.matchingHashCode(), k -> new ArrayList<>());

        Entry<K, V> lastEntry = null;
        for (Entry<K, V> entry : entries) {
            if (key.matches(entry.getKey())) {
                lastEntry = entry;
                break;
            }
        }

        entries.remove(lastEntry);
        entries.add(new SimpleEntry<>(key, value));
        return lastEntry == null ? null : lastEntry.getValue();
    }

    public void putAll(MatchingMap<K, V> matchingMap) {
        delegate.putAll(matchingMap.delegate);
    }

    @SuppressWarnings("unchecked")
    public V get(Object key) {
        K givenKey = (K) key;
        Entry<K, V> entry = this.getEntry(givenKey.matchingHashCode(), givenKey);
        return entry == null ? null : entry.getValue();
    }

    @SuppressWarnings("unchecked")
    public V remove(Object key) {
        K givenKey = (K) key;
        int matchingHashCode = givenKey.matchingHashCode();
        Entry<K, V> entry = this.getEntry(matchingHashCode, givenKey);

        if (entry == null)
            return null;

        delegate.get(matchingHashCode).remove(entry);
        return entry.getValue();
    }

    private Entry<K, V> getEntry(int matchingHashCode, K key) {
        List<Entry<K, V>> entries = delegate.get(matchingHashCode);

        if (entries == null)
            return null;

        for (Entry<K, V> entry : entries) {
            if (key.matches(entry.getKey()))
                return entry;
        }
        return null;
    }

    public void clear() {
        delegate.clear();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MatchingMap)) return false;
        MatchingMap<?, ?> that = (MatchingMap<?, ?>) o;
        return Objects.equals(delegate, that.delegate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(delegate);
    }
}
