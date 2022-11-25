package st.networkers.rimor.inject;

import com.google.common.reflect.TypeToken;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import st.networkers.rimor.internal.inject.AbstractAnnotated;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Exchanged in {@link Injector} to obtain an object for the wrapped type {@link T}.
 * <p>
 * For generic types, use the {@link TypeToken} constructor. For example, to construct a Token of type
 * {@literal List<String>} annotated with {@code @Params}, use:
 * <pre>
 *     new Token<>(new{@literal TypeToken<List<String>>}() {}).annotatedWith(Params.class);
 * </pre>
 */
@Getter
@EqualsAndHashCode(callSuper = true)
public class Token<T> extends AbstractAnnotated<Token<T>> {

    private final TypeToken<T> type;

    /**
     * Constructs a Token of a given type.
     *
     * @param type the type of the token
     */
    public Token(Class<T> type) {
        this(type, new HashMap<>());
    }

    /**
     * Constructs a Token of a given type.
     *
     * @param type the type of the token
     */
    public Token(TypeToken<T> type) {
        this(type, new HashMap<>());
    }

    public Token(Class<T> type, Map<Class<? extends Annotation>, Annotation> annotations) {
        this(type, annotations, new ArrayList<>());
    }

    public Token(TypeToken<T> type, Map<Class<? extends Annotation>, Annotation> annotations) {
        this(type, annotations, new ArrayList<>());
    }

    public Token(Class<T> type,
                 Map<Class<? extends Annotation>, Annotation> annotations,
                 Collection<Class<? extends Annotation>> requiredAnnotations
    ) {
        this(TypeToken.of(type), annotations, requiredAnnotations);
    }

    public Token(TypeToken<T> type,
                 Map<Class<? extends Annotation>, Annotation> annotations,
                 Collection<Class<? extends Annotation>> requiredAnnotations
    ) {
        super(annotations, requiredAnnotations);
        this.type = type.wrap();
    }
}
