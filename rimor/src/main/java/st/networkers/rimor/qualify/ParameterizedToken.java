package st.networkers.rimor.qualify;

import org.apache.commons.lang3.reflect.TypeUtils;

import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.Map;

/**
 * Represents a qualified parameterized type.
 *
 * <p>Due to type erasure in Java, it's necessary to create an anonymous implementation to keep
 * type arguments at runtime.
 *
 * <p>For example, to represent a {@code List<String>} qualified with any {@code @MyQualifier}, use:
 * <pre>
 * new ParameterizedToken&lt;List&lt;String>>() {}.qualifiedWith(MyQualifier.class);
 * </pre>
 *
 * @param <T> the type represented by the token
 */
public abstract class ParameterizedToken<T> extends Token {

    /**
     * Constructs a Token for the type {@link T}.
     */
    @SuppressWarnings("rawtypes")
    protected ParameterizedToken() {
        // we can get the type arguments of the anonymous implementation by comparing it with the abstract class
        Class<? extends ParameterizedToken> implementation = this.getClass();
        Map<TypeVariable<?>, Type> typeArguments = TypeUtils.getTypeArguments(implementation, ParameterizedToken.class);

        // the type of the token is the first (and only) parameter, T.
        this.type = typeArguments.get(ParameterizedToken.class.getTypeParameters()[0]);
    }
}
