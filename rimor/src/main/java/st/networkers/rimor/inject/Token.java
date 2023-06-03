package st.networkers.rimor.inject;

import com.google.common.reflect.TypeToken;
import st.networkers.rimor.annotated.AnnotatedProperties;
import st.networkers.rimor.annotated.DinamicallyAnnotated;

import java.lang.annotation.Annotation;
import java.util.Objects;

/**
 * Exchanged in {@link RimorInjector} to obtain an object for the wrapped type {@link T}.
 * <p>
 * For generic types, use the {@link TypeToken} constructor. For example, to construct a Token of type
 * {@literal List<String>} annotated with {@code @Params}, use:
 * <pre>
 * new Token<>(new{@literal TypeToken<List<String>>}() {}).annotatedWith(Params.class);
 * </pre>
 */
public class Token<T> extends DinamicallyAnnotated<Token<T>> {

    private final TypeToken<T> type;

    /**
     * Constructs a Token of a given type.
     *
     * @param type the type of the token
     */
    public Token(Class<T> type) {
        this(TypeToken.of(type));
    }

    /**
     * Constructs a Token of a given type.
     *
     * @param type the type of the token
     */
    public Token(TypeToken<T> type) {
        this.type = type.wrap();
    }

    /**
     * Constructs a Token of a given type with the specified {@link AnnotatedProperties}.
     * <p>
     * For a programmatic use of Token, use the {@link Token#Token(Class)} or {@link Token#Token(TypeToken)} constructor
     * along with the {@link #annotatedWith(Annotation)} and {@link #annotatedWith(Class)} methods.
     *
     * @param type the type of the token
     */
    public Token(Class<T> type, AnnotatedProperties annotatedProperties) {
        this(TypeToken.of(type), annotatedProperties);
    }

    /**
     * Constructs a Token of a given type with the specified {@link AnnotatedProperties}.
     * <p>
     * For a programmatic use of Token, use the {@link Token#Token(Class)} or {@link Token#Token(TypeToken)} constructor
     * along with the {@link #annotatedWith(Annotation)} and {@link #annotatedWith(Class)} methods.
     *
     * @param type the type of the token
     */
    public Token(TypeToken<T> type, AnnotatedProperties annotatedProperties) {
        super(annotatedProperties);
        this.type = type.wrap();
    }

    public TypeToken<T> getType() {
        return type;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Token)) return false;
        if (!super.equals(o)) return false;
        Token<?> token = (Token<?>) o;
        return Objects.equals(type, token.type);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), type);
    }
}
