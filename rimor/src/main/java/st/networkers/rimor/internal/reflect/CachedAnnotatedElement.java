package st.networkers.rimor.internal.reflect;

import java.lang.annotation.Annotation;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;

public class CachedAnnotatedElement {

    private final Map<Class<? extends Annotation>, Annotation> annotations;

    public CachedAnnotatedElement(Map<Class<? extends Annotation>, Annotation> annotations) {
        this.annotations = annotations;
    }

    public boolean isAnnotationPresent(Class<? extends Annotation> annotationClass) {
        return this.getAnnotation(annotationClass) != null;
    }

    @SuppressWarnings("unchecked")
    public <T extends Annotation> T getAnnotation(Class<T> annotationClass) {
        return (T) annotations.get(annotationClass);
    }

    public Collection<Annotation> getAnnotations() {
        return annotations.values();
    }

    public Map<Class<? extends Annotation>, Annotation> getAnnotationsMap() {
        return Collections.unmodifiableMap(this.annotations);
    }
}
