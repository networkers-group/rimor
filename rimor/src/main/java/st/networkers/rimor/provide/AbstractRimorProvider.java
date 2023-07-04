package st.networkers.rimor.provide;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.ClassUtils;
import st.networkers.rimor.annotated.AnnotatedProperties;
import st.networkers.rimor.annotated.DinamicallyAnnotated;
import st.networkers.rimor.context.ExecutionContext;
import st.networkers.rimor.inject.Token;

import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Useful abstract class to implement {@link RimorProvider}s.
 *
 * <p>For example, this is the provider for an imaginary {@code @Stats}-annotated {@code UserStats}, obtained from an
 * imaginary {@code @Sender}-annotated {@code User} present in the {@link ExecutionContext execution context}:
 * <pre>
 * public class UserStatsProvider extends AbstractRimorProvider&lt;UserStats> {
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
public abstract class AbstractRimorProvider<T>
        extends DinamicallyAnnotated<AbstractRimorProvider<T>>
        implements RimorProvider<T> {

    private final Collection<Type> providedTypes;

    @SafeVarargs
    protected AbstractRimorProvider(Class<? extends T> providedType, Class<? extends T>... otherTypes) {
        this(Stream.concat(Stream.of(providedType), Arrays.stream(ArrayUtils.add(otherTypes, providedType)))
                .map(ClassUtils::primitiveToWrapper)
                .collect(Collectors.toList()));
    }

    protected AbstractRimorProvider(Type providedType, Type... otherTypes) {
        this(Stream.concat(Stream.of(providedType), Arrays.stream(otherTypes)).collect(Collectors.toList()));
    }

    private AbstractRimorProvider(Collection<Type> providedTypes) {
        this.providedTypes = providedTypes;
        this.withProperties(this.inspectAnnotations());
    }

    @Override
    public Collection<Type> getProvidedTypes() {
        return providedTypes;
    }

    private AnnotatedProperties inspectAnnotations() {
        try {
            return AnnotatedProperties.build(this.getClass().getMethod("get", Token.class, ExecutionContext.class))
                    .merge(AnnotatedProperties.build(this.getClass()));
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }
}
