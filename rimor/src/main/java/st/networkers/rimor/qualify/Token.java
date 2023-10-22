package st.networkers.rimor.qualify;

import org.apache.commons.lang3.reflect.TypeUtils;
import st.networkers.rimor.util.MatchingKey;
import st.networkers.rimor.util.ReflectionUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.*;

/**
 * Represents a qualified {@link Type}.
 * <p>For example, to represent a {@link String} qualified with any {@code @MyAnnotation} qualifier, use:
 * <pre>
 * Token.of(String.class).qualifiedWith(MyAnnotation.class);
 * </pre>
 * To represent parameterized types, like {@code List<String>}, check {@link ParameterizedToken}.
 *
 * @see ParameterizedToken
 */
public class Token extends DinamicallyQualified<Token> implements MatchingKey {

    /**
     * Builds a {@code Token} for the provided type.
     *
     * @param type the type represented by this {@code Token}
     */
    public static Token of(Type type) {
        return new Token(type);
    }

    /**
     * Builds a copy of the provided {@code Token}.
     *
     * @param token the token to copy
     */
    public static Token copyOf(Token token) {
        return of(token.getType(), token.getQualifiersMap(), token.getRequiredQualifiers());
    }

    /**
     * Builds a {@code Token} for the provided type.
     *
     * @param type the type represented by this {@code Token}
     */
    public static Token of(Type type,
                           Map<Class<? extends Annotation>, Annotation> annotations,
                           Collection<Class<? extends Annotation>> requiredAnnotations) {
        return new Token(type, annotations, requiredAnnotations);
    }

    protected Type type;

    protected Token() {
    }

    protected Token(Type type) {
        this.type = ReflectionUtils.wrapPrimitive(type);
    }

    protected Token(Type type,
                    Map<Class<? extends Annotation>, Annotation> qualifiers,
                    Collection<Class<? extends Annotation>> requiredQualifiers) {
        super(qualifiers, requiredQualifiers);
        this.type = ReflectionUtils.wrapPrimitive(type);
    }

    /**
     * Whether the given {@code Token}'s type is assignable to this {@code Token}'s type and this {@code Token}
     * contains all the annotations (instances and required types) of the given {@code Token}.
     *
     * @param other the {@code Token} to check if is assignable to this {@code Token}
     * @return if the given {@code Token} is assignable to this {@code Token}
     */
    public boolean isAssignableFrom(Token other) {
        return TypeUtils.isAssignable(other.getType(), this.type)
               && this.containsAllQualifiersOf(other, AssignCriteria.EQUALS);
    }

    public Type getType() {
        return type;
    }

    @Override
    public boolean matches(MatchingKey o) {
        if (this == o) return true;
        if (!(o instanceof Token)) return false;
        Token token = (Token) o;
        return this.type.equals(token.type) && this.containsAllQualifiersOf(token, AssignCriteria.EQUALS);
    }

    @Override
    public int matchingHashCode() {
        Set<Class<? extends Annotation>> annotationTypes = new HashSet<>(this.getRequiredQualifiers());
        annotationTypes.addAll(this.getQualifiersMap().keySet());

        return Objects.hash(this.type, annotationTypes);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Token)) return false;
        if (!super.equals(o)) return false;
        Token token = (Token) o;
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
               ", annotations=" + qualifiers +
               ", requiredAnnotations=" + requiredQualifiers +
               '}';
    }
}
