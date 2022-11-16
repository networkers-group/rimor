package st.networkers.rimor.provide;

import com.google.common.reflect.TypeToken;
import st.networkers.rimor.context.ExecutionContext;
import st.networkers.rimor.inject.Injector;
import st.networkers.rimor.inject.Token;
import st.networkers.rimor.internal.inject.AbstractAnnotated;
import st.networkers.rimor.util.InspectionUtils;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.stream.Collectors;

/**
 * Useful abstract class to implement {@link RimorProvider}s.
 * <p>
 * For example, to provide an imaginary {@code User} annotated with {@code @UserData} from an imaginary
 * {@code CommandSender} always present in any {@link ExecutionContext} annotated with {@code @Sender}:
 *
 * <pre>
 * public class UserProvider extends {@literal AbstractRimorProvider<User>} {
 *
 *     // from injection, passing as parameter... whatever.
 *     private final {@literal Database<User>} userDatabase;
 *
 *     public UserProvider() {
 *         super(User.class); // the provided types, also can use TypeToken
 *         annotatedWith(UserData.class);
 *     }
 *
 *     &#64;Override
 *     public User get({@literal Token<User>} token, Injector injector, ExecutionContext context) {
 *         {@literal Token<CommandSender>} senderToken = new Token<>(CommandSender.class)
 *                 .annotatedWith(Sender.class);
 *
 *         CommandSender sender = context.get(senderToken)
 *                 .orElseThrow(new IllegalArgumentException());
 *
 *         return userDatabase.fromId(sender.getId());
 *     }
 * }
 * </pre>
 */
public abstract class AbstractRimorProvider<T>
        extends AbstractAnnotated<AbstractRimorProvider<T>>
        implements RimorProvider<T> {

    private final Collection<TypeToken<T>> providedTypes;

    @SafeVarargs
    protected AbstractRimorProvider(Class<T>... providedTypes) {
        this(Arrays.stream(providedTypes).map(TypeToken::of).collect(Collectors.toList()));
    }

    @SafeVarargs
    protected AbstractRimorProvider(TypeToken<T>... providedTypes) {
        this(Arrays.asList(providedTypes));
    }

    protected AbstractRimorProvider(Collection<TypeToken<T>> providedTypes) {
        this.providedTypes = providedTypes;
        this.inspectAnnotations();
    }

    @Override
    public Collection<TypeToken<T>> getProvidedTypes() {
        return providedTypes;
    }

    @Override
    public boolean canProvide(Token<?> token, Injector injector, ExecutionContext context) {
        for (TypeToken<T> providedType : this.providedTypes)
            if (token.getType().isSupertypeOf(providedType))
                return this.matchesAnnotations(token) && token.matchesAnnotations(this);
        return false;
    }

    private void inspectAnnotations() {
        Method method;
        try {
            method = this.getClass().getMethod("get", Token.class, Injector.class, ExecutionContext.class);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }

        if (method.isAnnotationPresent(RequireAnnotations.class))
            Collections.addAll(this.requiredAnnotations, method.getAnnotation(RequireAnnotations.class).value());

        this.annotations.putAll(InspectionUtils.getMappedAnnotations(
                Arrays.stream(method.getAnnotations())
                        .filter(annotation -> !(annotation instanceof RequireAnnotations))
                        .collect(Collectors.toList())
        ));
    }
}
