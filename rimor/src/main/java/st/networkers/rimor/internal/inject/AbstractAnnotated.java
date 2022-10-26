package st.networkers.rimor.internal.inject;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public abstract class AbstractAnnotated<T extends AbstractAnnotated<T>> implements Annotated {

    protected Map<Class<? extends Annotation>, Annotation> annotations;
    protected Collection<Class<? extends Annotation>> requiredAnnotations;

    protected AbstractAnnotated() {
        this(new HashMap<>(), new ArrayList<>());
    }

    protected AbstractAnnotated(Map<Class<? extends Annotation>, Annotation> annotations, Collection<Class<? extends Annotation>> requiredAnnotations) {
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

    @Override
    public boolean matchesAnnotations(Annotated annotated) {
        for (Class<? extends Annotation> annotation : annotated.getRequiredAnnotations())
            if (!this.isAnnotationPresent(annotation))
                return false;

        for (Annotation annotation : annotated.getAnnotations())
            if (!this.isAnnotationPresent(annotation))
                return false;

        return this.annotations.size() + this.requiredAnnotations.size()
               == annotated.getAnnotations().size() + annotated.getRequiredAnnotations().size();
    }

    @Override
    public Collection<Annotation> getAnnotations() {
        return annotations.values();
    }

    @Override
    public Collection<Class<? extends Annotation>> getRequiredAnnotations() {
        return requiredAnnotations;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <A extends Annotation> A getAnnotation(Class<A> annotationClass) {
        return (A) this.annotations.get(annotationClass);
    }

    @Override
    public boolean isAnnotationPresent(Annotation annotation) {
        return annotations.containsValue(annotation) || requiredAnnotations.contains(annotation.annotationType());
    }

    @Override
    public boolean isAnnotationPresent(Class<? extends Annotation> annotation) {
        return this.annotations.containsKey(annotation) || requiredAnnotations.contains(annotation);
    }

    @SuppressWarnings("unchecked")
    private T impl() {
        return (T) this;
    }
}
