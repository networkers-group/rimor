package st.networkers.rimor.inject;

import org.apache.commons.lang3.ClassUtils;
import org.apache.commons.lang3.reflect.TypeUtils;
import st.networkers.rimor.annotated.Annotated;
import st.networkers.rimor.annotated.AnnotatedProperties;
import st.networkers.rimor.annotated.DinamicallyAnnotated;
import st.networkers.rimor.util.MatchingKey;
import st.networkers.rimor.util.ReflectionUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * Represents an annotated {@link Type}.
 * <p>For example, to represent a {@link String} annotated with any {@code @MyAnnotation}, use:
 * <pre>
 * Token.of(String.class).annotatedWith(MyAnnotation.class);
 * </pre>
 * For generic types, use an empty anonymous inner class along with the {@link Token#Token()} constructor.
 * For example, to build a Token of type {@code List<String>} annotated with any {@code @MyAnnotation}, use:
 * <pre>
 * new Token&lt;List&lt;String>>() {}.annotatedWith(MyAnnotation.class);
 * </pre>
 */
public class Token<T> extends DinamicallyAnnotated<Token<T>> implements MatchingKey {

    /**
     * Builds a Token for the provided type.
     *
     * @param type the type of the token
     */
    public static <T> Token<T> of(Class<T> type) {
        return of((Type) ClassUtils.primitiveToWrapper(type));
    }

    /**
     * Builds a Token for the provided type.
     *
     * @param type the type of the token
     */
    public static <T> Token<T> of(Type type) {
        return new Token<>(type);
    }

    /**
     * Builds a Token for the provided type with the specified {@link AnnotatedProperties}.
     *
     * @param type                the type of the token
     * @param annotatedProperties the {@link AnnotatedProperties} of the token
     */
    public static <T> Token<T> of(Type type, AnnotatedProperties annotatedProperties) {
        return new Token<>(type, annotatedProperties);
    }

    private final Type type;

    /**
     * Constructs a Token for the type {@link T}.
     */
    protected Token() {
        this.type = TypeUtils.getTypeArguments(getClass(), Token.class).get(Token.class.getTypeParameters()[0]);
    }

    protected Token(Type type) {
        this.type = ReflectionUtils.wrapPrimitive(type);
    }

    protected Token(Type type, AnnotatedProperties annotatedProperties) {
        super(annotatedProperties);
        this.type = ReflectionUtils.wrapPrimitive(type);
    }

    public boolean isAssignableFrom(Token<?> other) {
        return TypeUtils.isAssignable(other.getType(), this.type) && this.isAssignableFrom(other, AssignCriteria.EQUALS);
    }

    public Type getType() {
        return type;
    }

    @Override
    public boolean matches(MatchingKey o) {
        if (this == o) return true;
        if (!(o instanceof Token)) return false;
        Token<?> token = (Token<?>) o;
        return this.type.equals(token.type) && this.isAssignableFrom(token, Annotated.AssignCriteria.EQUALS);
    }

    @Override
    public int matchingHashCode() {
        Set<Class<? extends Annotation>> annotationTypes = new HashSet<>(this.getRequiredAnnotations());
        annotationTypes.addAll(this.getAnnotatedProperties().getAnnotations().keySet());

        return Objects.hash(this.type, annotationTypes);
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

    @Override
    public String toString() {
        return "Token{" +
               "type=" + type +
               ", annotatedProperties=" + getAnnotatedProperties() +
               '}';
    }
}
