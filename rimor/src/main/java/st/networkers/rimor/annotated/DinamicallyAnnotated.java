package st.networkers.rimor.annotated;

import java.lang.annotation.Annotation;
import java.util.*;

/**
 * Abstract class for objects to be able to be programmatically annotated at runtime.
 */
public abstract class DinamicallyAnnotated<T extends DinamicallyAnnotated<T>> implements Annotated {

    private final Map<Class<? extends Annotation>, Annotation> annotations;
    private final Collection<Class<? extends Annotation>> requiredAnnotations;

    public DinamicallyAnnotated() {
        this.annotations = new HashMap<>();
        this.requiredAnnotations = new ArrayList<>();
    }

    public DinamicallyAnnotated(AnnotatedProperties properties) {
        this();
        this.annotations.putAll(properties.getAnnotations());
        this.requiredAnnotations.addAll(properties.getRequiredAnnotations());
    }

    public T annotatedWith(Annotation annotation) {
        this.annotations.put(annotation.annotationType(), annotation);
        return this.casted();
    }

    public T annotatedWith(Class<? extends Annotation> requiredAnnotation) {
        this.requiredAnnotations.add(requiredAnnotation);
        return this.casted();
    }

    public T withProperties(AnnotatedProperties properties) {
        this.annotations.putAll(properties.getAnnotations());
        this.requiredAnnotations.addAll(properties.getRequiredAnnotations());
        return this.casted();
    }

    @SuppressWarnings("unchecked")
    public T casted() {
        return (T) this;
    }

    @Override
    public AnnotatedProperties getAnnotatedProperties() {
        return new AnnotatedProperties(this.annotations, this.requiredAnnotations);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DinamicallyAnnotated)) return false;
        DinamicallyAnnotated<?> that = (DinamicallyAnnotated<?>) o;
        return Objects.equals(annotations, that.annotations) && Objects.equals(requiredAnnotations, that.requiredAnnotations);
    }

    @Override
    public int hashCode() {
        return Objects.hash(annotations, requiredAnnotations);
    }
}
