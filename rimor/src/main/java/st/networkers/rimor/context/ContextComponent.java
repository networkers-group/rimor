package st.networkers.rimor.context;

import com.google.common.reflect.TypeToken;
import lombok.Getter;
import st.networkers.rimor.internal.inject.Annotated;
import st.networkers.rimor.internal.inject.Token;

@Getter
public class ContextComponent<T> extends Annotated<ContextComponent<T>> {

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

    public boolean canProvide(Token<? super T> token) {
        return token.getType().isSubtypeOf(this.type) && this.matchesAnnotations(token);
    }
}
