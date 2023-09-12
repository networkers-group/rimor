package st.networkers.rimor.qualify;

import st.networkers.rimor.reflect.CachedQualifiedElement;

import java.lang.annotation.Annotation;
import java.util.*;

/**
 * Abstract class to allow adding {@link RimorQualifier qualifier} annotations to elements at runtime.
 */
public abstract class DinamicallyQualified<T extends DinamicallyQualified<T>> implements Qualified {

    protected final Map<Class<? extends Annotation>, Annotation> annotations;
    protected final Collection<Class<? extends Annotation>> requiredAnnotations;

    protected DinamicallyQualified() {
        this(new HashMap<>(), new ArrayList<>());
    }

    protected DinamicallyQualified(Map<Class<? extends Annotation>, Annotation> annotations,
                                   Collection<Class<? extends Annotation>> requiredAnnotations) {
        this.annotations = annotations;
        this.requiredAnnotations = requiredAnnotations;
    }

    public T annotatedWith(Annotation annotation) {
        this.annotations.put(annotation.annotationType(), annotation);
        return this.casted();
    }

    public T annotatedWith(Class<? extends Annotation> requiredAnnotation) {
        this.requiredAnnotations.add(requiredAnnotation);
        return this.casted();
    }

    public T withAnnotations(Map<Class<? extends Annotation>, Annotation>  annotations) {
        this.annotations.putAll(annotations);
        return this.casted();
    }

    public T withRequiredAnnotations(Collection<Class<? extends Annotation>> requiredAnnotations) {
        this.requiredAnnotations.addAll(requiredAnnotations);
        return this.casted();
    }

    public T withAnnotationsOf(CachedQualifiedElement element) {
        this.withAnnotations(element.getAnnotationsMap());
        this.withRequiredAnnotations(element.getRequiredAnnotations());
        return this.casted();
    }

    @Override
    public Map<Class<? extends Annotation>, Annotation> getAnnotationsMap() {
        return annotations;
    }

    @Override
    public Collection<Class<? extends Annotation>> getRequiredAnnotations() {
        return requiredAnnotations;
    }

    @SuppressWarnings("unchecked")
    public T casted() {
        return (T) this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DinamicallyQualified)) return false;
        DinamicallyQualified<?> that = (DinamicallyQualified<?>) o;
        return Objects.equals(annotations, that.annotations) && Objects.equals(requiredAnnotations, that.requiredAnnotations);
    }

    @Override
    public int hashCode() {
        return Objects.hash(annotations, requiredAnnotations);
    }
}
