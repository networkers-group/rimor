package st.networkers.rimor.inject;

import com.google.common.reflect.TypeToken;

import java.util.Objects;

/**
 * Wraps an object of type {@link T} to inject in instruction handlers. Every component be annotated to distinguish
 * components of the same type. For example:
 * <pre>
 * // to distinguish between a command sender and the user the sender is replying to:
 * &#064;InstructionMapping("myInstruction")
 * public void myInstruction(@CommandSender User user,
 *                           &#064;ReplyingTo User replyingTo) {
 *     ...
 * }
 *
 * // the {@link ContextComponent} would have:
 * // @CommandSender annotated User
 * ... = new ContextComponent<>(User.class, sender)
 *     .annotatedWith(CommandSender.class);
 *
 * // @ReplyingTo annotated User
 * ... = new ContextComponent<>(User.class, replyingTo)
 *     .annotatedWith(ReplyingTo.class);
 * </pre>
 */
public class ContextComponent<T> extends DinamicallyAnnotated<ContextComponent<T>> {

    private final TypeToken<T> type;
    private final T object;

    /**
     * Constructs a ContextComponent that needs no annotations to be injected
     *
     * @param type   the type to inject
     * @param object the instance to inject
     */
    public ContextComponent(Class<T> type, T object) {
        this(TypeToken.of(type), object);
    }

    /**
     * Constructs a ContextComponent that needs no annotations to be injected
     *
     * @param type   the type to inject
     * @param object the instance to inject
     */
    public ContextComponent(TypeToken<T> type, T object) {
        this.type = type.wrap();
        this.object = object;
    }

    public boolean canProvide(Token<?> token) {
        return token.getType().isSupertypeOf(this.type)
               && this.matchesAnnotations(token)
               && token.matchesAnnotations(this);
    }

    public TypeToken<T> getType() {
        return type;
    }

    public T getObject() {
        return object;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ContextComponent)) return false;
        if (!super.equals(o)) return false;
        ContextComponent<?> that = (ContextComponent<?>) o;
        return Objects.equals(type, that.type) && Objects.equals(object, that.object);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), type, object);
    }
}
