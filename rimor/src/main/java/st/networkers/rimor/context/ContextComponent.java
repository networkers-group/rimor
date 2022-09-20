package st.networkers.rimor.context;

import lombok.Getter;
import org.jetbrains.annotations.Nullable;
import st.networkers.rimor.internal.reflect.CachedParameter;

import java.lang.annotation.Annotation;

public class ContextComponent {

    @Nullable private final Annotation annotation;
    @Nullable private final Class<? extends Annotation> annotationClass;

    @Getter private final Class<?> type;
    @Getter private final Object object;

    /**
     * Constructs a ContextComponent that needs no annotations to be injected
     *
     * @param type   the type to inject
     * @param object the instance to inject
     */
    public ContextComponent(Class<?> type, Object object) {
        this(null, null, type, object);
    }

    /**
     * Constructs a ContextComponent that needs the given annotation to be present to be injected
     *
     * @param annotationClass the annotation that needs to be present
     * @param type            the type to inject
     * @param object          the instance to inject
     */
    public ContextComponent(Class<? extends Annotation> annotationClass, Class<?> type, Object object) {
        this(null, annotationClass, type, object);
    }

    /**
     * Constructs a ContextComponent that needs the given annotation to be present to be injected
     *
     * @param annotation the annotation that needs to be present
     * @param type       the type to inject
     * @param object     the instance to inject
     */
    public ContextComponent(Annotation annotation, Class<?> type, Object object) {
        this(annotation, null, type, object);
    }

    private ContextComponent(@Nullable Annotation annotation,
                             @Nullable Class<? extends Annotation> annotationClass,
                             Class<?> type,
                             Object object) {
        this.annotation = annotation;
        this.annotationClass = annotationClass;
        this.type = type;
        this.object = object;
    }

    public boolean canProvide(CachedParameter parameter) {
        return parameter.getType().isInstance(this.object) && this.annotationMatches(parameter);
    }

    private boolean annotationMatches(CachedParameter parameter) {
        if (this.annotation != null)
            return parameter.getAnnotations().contains(this.annotation);

        if (this.annotationClass != null)
            return parameter.isAnnotationPresent(this.annotationClass);

        return parameter.getAnnotations().isEmpty();
    }
}
