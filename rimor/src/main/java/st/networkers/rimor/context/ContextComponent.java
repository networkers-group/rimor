package st.networkers.rimor.context;

import com.google.common.reflect.TypeToken;
import lombok.Getter;
import org.jetbrains.annotations.Nullable;
import st.networkers.rimor.internal.inject.Token;

import java.lang.annotation.Annotation;

public class ContextComponent<T> {

    @Nullable private final Annotation annotation;
    @Nullable private final Class<? extends Annotation> annotationClass;

    @Getter private final TypeToken<T> type;
    @Getter private final T object;

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
        this(null, null, type, object);
    }

    /**
     * Constructs a ContextComponent that needs the given annotation to be present to be injected
     *
     * @param annotationClass the annotation that needs to be present
     * @param type            the type to inject
     * @param object          the instance to inject
     */
    public ContextComponent(Class<? extends Annotation> annotationClass, Class<T> type, T object) {
        this(null, annotationClass, TypeToken.of(type), object);
    }

    /**
     * Constructs a ContextComponent that needs the given annotation to be present to be injected
     *
     * @param annotationClass the annotation that needs to be present
     * @param type            the type to inject
     * @param object          the instance to inject
     */
    public ContextComponent(Class<? extends Annotation> annotationClass, TypeToken<T> type, T object) {
        this(null, annotationClass, type, object);
    }

    /**
     * Constructs a ContextComponent that needs the given annotation to be present to be injected
     *
     * @param annotation the annotation that needs to be present
     * @param type       the type to inject
     * @param object     the instance to inject
     */
    public ContextComponent(Annotation annotation, Class<T> type, T object) {
        this(annotation, null, TypeToken.of(type), object);
    }

    /**
     * Constructs a ContextComponent that needs the given annotation to be present to be injected
     *
     * @param annotation the annotation that needs to be present
     * @param type       the type to inject
     * @param object     the instance to inject
     */
    public ContextComponent(Annotation annotation, TypeToken<T> type, T object) {
        this(annotation, null, type, object);
    }

    private ContextComponent(@Nullable Annotation annotation,
                             @Nullable Class<? extends Annotation> annotationClass,
                             TypeToken<T> type,
                             T object) {
        this.annotation = annotation;
        this.annotationClass = annotationClass;
        this.type = type;
        this.object = object;
    }

    public boolean canProvide(Token<? super T> token) {
        return token.getType().getRawType().isInstance(this.object) && this.annotationMatches(token);
    }

    private boolean annotationMatches(Token<? super T> token) {
        if (this.annotation != null)
            return token.getAnnotations().containsValue(this.annotation);

        if (this.annotationClass != null)
            return token.getAnnotations().containsKey(this.annotationClass);

        return token.getAnnotations().isEmpty();
    }
}
