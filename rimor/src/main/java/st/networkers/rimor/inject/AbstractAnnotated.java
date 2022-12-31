package st.networkers.rimor.inject;

import st.networkers.rimor.util.InspectionUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.util.*;
import java.util.stream.Collectors;

public abstract class AbstractAnnotated<T extends AbstractAnnotated<T>> implements Annotated {

    protected Map<Class<? extends Annotation>, Annotation> annotations;
    protected Collection<Class<? extends Annotation>> requiredAnnotations;

    protected AbstractAnnotated() {
        this(new HashMap<>(), new ArrayList<>());
    }

    protected AbstractAnnotated(Map<Class<? extends Annotation>, Annotation> annotations) {
        this(annotations, new ArrayList<>());
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

        return true;
    }

    @Override
    public Collection<Annotation> getAnnotations() {
        return annotations.values();
    }

    @Override
    public Map<Class<? extends Annotation>, Annotation> getMappedAnnotations() {
        return Collections.unmodifiableMap(annotations);
    }

    @Override
    public Collection<Class<? extends Annotation>> getRequiredAnnotations() {
        return Collections.unmodifiableCollection(requiredAnnotations);
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

    protected void inspectAnnotations(AnnotatedElement annotatedElement) {
        if (annotatedElement.isAnnotationPresent(RequireAnnotations.class))
            Collections.addAll(this.requiredAnnotations, annotatedElement.getAnnotation(RequireAnnotations.class).value());

        this.annotations.putAll(InspectionUtils.getMappedAnnotations(
                Arrays.stream(annotatedElement.getAnnotations())
                        .filter(annotation -> !(annotation instanceof RequireAnnotations))
                        .collect(Collectors.toList())
        ));
    }

    @SuppressWarnings("unchecked")
    private T impl() {
        return (T) this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AbstractAnnotated)) return false;
        AbstractAnnotated<?> that = (AbstractAnnotated<?>) o;
        return Objects.equals(annotations, that.annotations) && Objects.equals(requiredAnnotations, that.requiredAnnotations);
    }

    @Override
    public int hashCode() {
        return Objects.hash(annotations, requiredAnnotations);
    }
}
