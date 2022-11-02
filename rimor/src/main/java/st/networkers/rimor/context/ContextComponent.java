package st.networkers.rimor.context;

import com.google.common.reflect.TypeToken;
import lombok.Getter;
import st.networkers.rimor.inject.Token;
import st.networkers.rimor.internal.inject.AbstractAnnotated;

@Getter
public class ContextComponent<T> extends AbstractAnnotated<ContextComponent<T>> {

    @Getter private final TypeToken<T> type;
    @Getter private final T object;

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
        this.type = type;
        this.object = object;
    }

    public boolean canProvide(Token<?> token) {
        return token.getType().isSupertypeOf(this.type)
               && this.matchesAnnotations(token)
               && token.matchesAnnotations(this);
    }
}
