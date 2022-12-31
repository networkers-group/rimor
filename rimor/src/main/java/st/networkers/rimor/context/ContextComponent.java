package st.networkers.rimor.context;

import com.google.common.reflect.TypeToken;
import st.networkers.rimor.inject.AbstractAnnotated;
import st.networkers.rimor.inject.Token;

import java.util.Objects;

/**
 * Wraps an object of type {@link T} to inject in instruction methods.
 */
public class ContextComponent<T> extends AbstractAnnotated<ContextComponent<T>> {

    private final TypeToken<T> type;
    private final T object;

    /**
     * Constructs a ContextComponent that needs no annotations to be injected
     *
     * @param type   the type to inject
     * @param object the instance to inject
     */
    public ContextComponent(Class<T> type, T object) {
        this(TypeToken.of(type), object);
    }

    /**
     * Constructs a ContextComponent that needs no annotations to be injected
     *
     * @param type   the type to inject
     * @param object the instance to inject
     */
    public ContextComponent(TypeToken<T> type, T object) {
        this.type = type.wrap();
        this.object = object;
    }

    public boolean canProvide(Token<?> token) {
        return token.getType().isSupertypeOf(this.type)
               && this.matchesAnnotations(token)
               && token.matchesAnnotations(this);
    }

    public TypeToken<T> getType() {
        return type;
    }

    public T getObject() {
        return object;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ContextComponent)) return false;
        if (!super.equals(o)) return false;
        ContextComponent<?> that = (ContextComponent<?>) o;
        return Objects.equals(type, that.type) && Objects.equals(object, that.object);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), type, object);
    }
}
