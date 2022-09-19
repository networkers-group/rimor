package st.networkers.rimor.context;

import org.jetbrains.annotations.Nullable;
import st.networkers.rimor.internal.reflect.CachedParameter;

import java.lang.annotation.Annotation;

public class ContextComponent {

    @Nullable private final Annotation annotation;
    @Nullable private final Class<? extends Annotation> annotationClass;

    private final Class<?> type;
    private final Object object;

    public ContextComponent(Class<?> type, Object object) {
        this(null, null, type, object);
    }

    public ContextComponent(Class<? extends Annotation> annotationClass, Class<?> type, Object object) {
        this(null, annotationClass, type, object);
    }

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

    public Class<?> getType() {
        return type;
    }

    public Object getObject() {
        return object;
    }

    public boolean canProvide(CachedParameter parameter) {
        return parameter.getType().isInstance(this.object) && this.annotationMatches(parameter);
    }

    private boolean annotationMatches(CachedParameter parameter) {
        if (this.annotation != null)
            return parameter.getAnnotations().contains(this.annotation);

        if (this.annotationClass != null)
            return parameter.isAnnotationPresent(this.annotationClass);

        return true;
    }
}
