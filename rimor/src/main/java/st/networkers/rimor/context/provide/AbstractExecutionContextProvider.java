package st.networkers.rimor.context.provide;

import st.networkers.rimor.context.ExecutionContext;
import st.networkers.rimor.qualify.DinamicallyQualified;
import st.networkers.rimor.qualify.Token;
import st.networkers.rimor.qualify.reflect.QualifiedClass;
import st.networkers.rimor.qualify.reflect.QualifiedMethod;
import st.networkers.rimor.util.ReflectionUtils;

import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Useful abstract class to implement {@link ExecutionContextProvider}s.
 *
 * <p>Check {@link ProvidesContext} for annotation-based execution context providers (recommended for most cases).
 *
 * <p>For example, this is the provider for an imaginary {@code @Stats}-qualified {@code UserStats}, obtained from an
 * imaginary {@code @Sender}-qualified {@code User} present in the {@link ExecutionContext execution context}:
 * <pre>
 * public class UserStatsProvider extends AbstractExecutionContextProvider&lt;UserStats> {
 *
 *     private final UserService userService = ...;
 *
 *     public UserProvider() {
 *         super(UserStats.class);
 *         qualifiedWith(Stats.class);
 *     }
 *
 *     &#64;Override
 *     public User get(Token&lt;UserStats> token, ExecutionContext context) {
 *         Token&lt;User> senderToken = Token.of(User.class)
 *                 .qualifiedWith(Sender.class);
 *
 *         CommandSender sender = context.get(senderToken)
 *                 .orElseThrow(() -> new IllegalArgumentException("there is no sender!?"));
 *
 *         return userService.computeStats(sender);
 *     }
 * }
 * </pre>
 * To sum up, the provider above would provide the {@code userStats} parameter for this method:
 * <pre>
 * public void myMethod(@Stats UserStats UserStats) {
 *     ...
 * }
 * </pre>
 */
public abstract class AbstractExecutionContextProvider<T>
        extends DinamicallyQualified<AbstractExecutionContextProvider<T>>
        implements ExecutionContextProvider<T> {

    private final Collection<Type> providedTypes;

    protected AbstractExecutionContextProvider(Type providedType, Type... otherTypes) {
        this(Stream.concat(Stream.of(providedType), Arrays.stream(otherTypes))
                .map(ReflectionUtils::wrapPrimitive)
                .collect(Collectors.toList()));
    }

    private AbstractExecutionContextProvider(Collection<Type> providedTypes) {
        this.addPresentAnnotations();
        this.providedTypes = providedTypes;
    }

    @Override
    public Collection<Type> getProvidedTypes() {
        return providedTypes;
    }

    private void addPresentAnnotations() {
        try {
            this.addQualifiersOf(QualifiedMethod.build(this.getClass().getMethod("get", Token.class, ExecutionContext.class)));
            this.addQualifiersOf(QualifiedClass.build(this.getClass()));
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName() + "{" +
               "providedTypes=" + providedTypes +
               ", annotations=" + qualifiers +
               ", requiredAnnotations=" + requiredQualifiers +
               '}';
    }
}
