package st.networkers.rimor.internal.inject;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public abstract class Annotated<T extends Annotated<T>> {

    protected Map<Class<? extends Annotation>, Annotation> annotations;
    protected Collection<Class<? extends Annotation>> requiredAnnotations;

    protected Annotated() {
        this(new HashMap<>(), new ArrayList<>());
    }

    protected Annotated(Map<Class<? extends Annotation>, Annotation> annotations, Collection<Class<? extends Annotation>> requiredAnnotations) {
        this.annotations = annotations;
        this.requiredAnnotations = requiredAnnotations;
    }

    public T annotatedWith(Annotation annotation) {
        this.annotations.put(annotation.annotationType(), annotation);
        return impl();
    }

    public T annotatedWith(Class<? extends Annotation> annotation) {
        this.requiredAnnotations.add(annotation);
        return impl();
    }

    public boolean matchesAnnotations(Annotated<?> annotated) {
        for (Class<? extends Annotation> annotation : annotated.requiredAnnotations)
            if (!this.containsAnnotation(annotation))
                return false;

        for (Annotation annotation : annotated.annotations.values())
            if (!this.containsAnnotation(annotation))
                return false;

        return this.annotations.size() + this.requiredAnnotations.size()
               == annotated.annotations.size() + annotated.requiredAnnotations.size();
    }

    @SuppressWarnings("unchecked")
    public <A extends Annotation> A getAnnotation(Class<A> annotationClass) {
        return (A) this.annotations.get(annotationClass);
    }

    private boolean containsAnnotation(Annotation annotation) {
        return annotations.containsValue(annotation) || requiredAnnotations.contains(annotation.annotationType());
    }

    private boolean containsAnnotation(Class<? extends Annotation> annotation) {
        return this.annotations.containsKey(annotation) || requiredAnnotations.contains(annotation);
    }

    @SuppressWarnings("unchecked")
    private T impl() {
        return (T) this;
    }
}
