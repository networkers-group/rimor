package st.networkers.rimor.util;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HashMultiMap<K, V> extends AbstractMultiMap<K, V> {

    public HashMultiMap() {
        this(new HashMap<>());
    }

    public HashMultiMap(Map<K, List<V>> m) {
        super(m);
    }
}
