package st.networkers.rimor.util;

import com.google.common.reflect.TypeToken;

import java.util.Map;
import java.util.Optional;

public class TypeMappingCache<K, V> extends MapAdapter<TypeToken<? extends K>, V> {

    private final TypeToken<K> topType;

    public TypeMappingCache(Map<TypeToken<? extends K>, V> delegate, TypeToken<K> topType) {
        super(delegate);
        this.topType = topType;
    }

    public V getIfPresent(Object o) {
        return super.get(o);
    }

    @Override
    @SuppressWarnings("unchecked")
    public V get(Object o) {
        TypeToken<? extends K> type = (TypeToken<? extends K>) o;
        if (!topType.isSupertypeOf(type))
            return null;

        return this.computeIfAbsent(type, t -> Optional.ofNullable(type.getRawType().getGenericSuperclass())
                .map(TypeToken::of)
                .map(this::get)
                .orElseGet(() -> this.getFromInterfaces(type)));
    }

    private V getFromInterfaces(TypeToken<?> type) {
        if (!topType.isAssignableFrom(type))
            return null;

        V value = this.getIfPresent(type);
        if (value != null)
            return value;

        for (Class<?> interfaceType : type.getInterfaces()) {
            if ((value = this.get(interfaceType)) != null)
                break;
        }

        return value;
    }
}
