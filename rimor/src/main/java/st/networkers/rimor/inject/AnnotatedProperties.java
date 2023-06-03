package st.networkers.rimor.inject;

import st.networkers.rimor.reflect.CachedAnnotatedElement;
import st.networkers.rimor.util.ReflectionUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.util.*;

/**
 * Contains the annotations and required annotation types of an {@link Annotated}.
 */
public class AnnotatedProperties {

    private final Map<Class<? extends Annotation>, Annotation> annotations;
    private final Collection<Class<? extends Annotation>> requiredAnnotations;

    public static AnnotatedProperties build() {
        return new AnnotatedProperties(new HashMap<>(), new ArrayList<>());
    }

    public static AnnotatedProperties build(CachedAnnotatedElement cachedAnnotatedElement) {
        return new AnnotatedProperties(cachedAnnotatedElement.getAnnotationsMap(), Collections.emptyList());
    }

    public static AnnotatedProperties build(AnnotatedElement annotatedElement) {
        return new AnnotatedProperties(
                ReflectionUtils.getMappedAnnotations(annotatedElement),
                Optional.ofNullable(annotatedElement.getAnnotation(RequireAnnotations.class))
                        .map(requireAnnotations -> Arrays.asList(requireAnnotations.value()))
                        .orElseGet(Collections::emptyList)
        );
    }

    public AnnotatedProperties(Map<Class<? extends Annotation>, Annotation> annotations,
                               Collection<Class<? extends Annotation>> requiredAnnotations) {
        this.annotations = annotations;
        this.requiredAnnotations = requiredAnnotations;
    }

    public Map<Class<? extends Annotation>, Annotation> getAnnotations() {
        return Collections.unmodifiableMap(this.annotations);
    }

    public Collection<Class<? extends Annotation>> getRequiredAnnotations() {
        return Collections.unmodifiableCollection(this.requiredAnnotations);
    }

    public AnnotatedProperties merge(AnnotatedProperties annotatedProperties) {
        return this.merge(annotatedProperties.getAnnotations(), annotatedProperties.getRequiredAnnotations());
    }

    public AnnotatedProperties merge(Map<Class<? extends Annotation>, Annotation> annotations,
                                     Collection<Class<? extends Annotation>> requiredAnnotations) {
        annotations = new HashMap<>(annotations);
        annotations.putAll(this.annotations);

        requiredAnnotations = new ArrayList<>(requiredAnnotations);
        requiredAnnotations.addAll(this.requiredAnnotations);

        return new AnnotatedProperties(annotations, requiredAnnotations);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AnnotatedProperties)) return false;
        AnnotatedProperties that = (AnnotatedProperties) o;
        return Objects.equals(annotations, that.annotations) && Objects.equals(requiredAnnotations, that.requiredAnnotations);
    }

    @Override
    public int hashCode() {
        return Objects.hash(annotations, requiredAnnotations);
    }
}
