package st.networkers.rimor.provide;

import com.google.common.reflect.TypeToken;
import st.networkers.rimor.context.ExecutionContext;
import st.networkers.rimor.inject.AbstractAnnotated;
import st.networkers.rimor.inject.Token;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Useful abstract class to implement {@link RimorProvider}s.
 * <p>
 * For example, to provide an imaginary {@code User} annotated with {@code @UserData} from an imaginary
 * {@code CommandSender} always present in any {@link ExecutionContext} annotated with {@code @Sender}:
 *
 * <pre>
 * public class UserProvider extends {@literal AbstractRimorProvider<User>} {
 *
 *     private final {@literal Database<User>} userDatabase = ...;
 *
 *     public UserProvider() {
 *         super(User.class); // the provided types, also can use TypeToken
 *         annotatedWith(UserData.class);
 *     }
 *
 *     &#64;Override
 *     public User get({@literal Token<User>} token, ExecutionContext context) {
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

    private final Collection<TypeToken<? extends T>> providedTypes;

    @SafeVarargs
    protected AbstractRimorProvider(Class<? extends T>... providedTypes) {
        this(Arrays.stream(providedTypes).map(TypeToken::of));
    }

    @SafeVarargs
    protected AbstractRimorProvider(TypeToken<? extends T>... providedTypes) {
        this(Arrays.stream(providedTypes));
    }

    protected AbstractRimorProvider(Collection<TypeToken<? extends T>> providedTypes) {
        this(providedTypes.stream());
    }

    private AbstractRimorProvider(Stream<TypeToken<? extends T>> providedTypes) {
        this.providedTypes = providedTypes.map(TypeToken::wrap).collect(Collectors.toList());
        this.inspectAnnotations();
    }

    @Override
    public Collection<TypeToken<? extends T>> getProvidedTypes() {
        return providedTypes;
    }

    @Override
    public boolean canProvide(Token<?> token, ExecutionContext context) {
        for (TypeToken<? extends T> providedType : this.providedTypes)
            if (token.getType().isSupertypeOf(providedType))
                return this.matchesAnnotations(token) && token.matchesAnnotations(this);
        return false;
    }

    private void inspectAnnotations() {
        try {
            Method method = this.getClass().getMethod("get", Token.class, ExecutionContext.class);
            this.inspectAnnotations(method);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }
}
