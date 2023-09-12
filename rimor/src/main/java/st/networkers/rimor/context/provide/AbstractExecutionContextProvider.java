package st.networkers.rimor.context.provide;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.ClassUtils;
import st.networkers.rimor.qualify.DinamicallyQualified;
import st.networkers.rimor.context.ExecutionContext;
import st.networkers.rimor.context.Token;
import st.networkers.rimor.reflect.CachedClass;
import st.networkers.rimor.reflect.CachedMethod;

import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Useful abstract class to implement {@link ExecutionContextProvider}s.
 *
 * <p>For example, this is the provider for an imaginary {@code @Stats}-annotated {@code UserStats}, obtained from an
 * imaginary {@code @Sender}-annotated {@code User} present in the {@link ExecutionContext execution context}:
 * <pre>
 * public class UserStatsProvider extends AbstractExecutionContextProvider&lt;UserStats> {
 *
 *     private final UserService userService = ...;
 *
 *     public UserProvider() {
 *         super(UserStats.class);
 *         annotatedWith(Stats.class);
 *     }
 *
 *     &#64;Override
 *     public User get(Token&lt;UserStats> token, ExecutionContext context) {
 *         Token&lt;User> senderToken = Token.of(User.class)
 *                 .annotatedWith(Sender.class);
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

    @SafeVarargs
    protected AbstractExecutionContextProvider(Class<? extends T> providedType, Class<? extends T>... otherTypes) {
        this(Stream.concat(Stream.of(providedType), Arrays.stream(ArrayUtils.add(otherTypes, providedType)))
                .map(ClassUtils::primitiveToWrapper)
                .collect(Collectors.toList()));
    }

    protected AbstractExecutionContextProvider(Type providedType, Type... otherTypes) {
        this(Stream.concat(Stream.of(providedType), Arrays.stream(otherTypes)).collect(Collectors.toList()));
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
            this.withAnnotationsOf(CachedMethod.build(this.getClass().getMethod("get", Token.class, ExecutionContext.class)))
                    .withAnnotationsOf(CachedClass.build(this.getClass()));
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }
}
