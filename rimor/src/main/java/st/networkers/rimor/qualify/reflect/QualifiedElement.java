package st.networkers.rimor.qualify.reflect;

import st.networkers.rimor.qualify.Qualified;
import st.networkers.rimor.qualify.RequireQualifiers;
import st.networkers.rimor.qualify.RimorQualifier;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class QualifiedElement implements Qualified {

    public static Map<Class<? extends Annotation>, Annotation> getMappedQualifiers(AnnotatedElement element) {
        return Arrays.stream(element.getAnnotations())
                .filter(annotation -> annotation.annotationType().isAnnotationPresent(RimorQualifier.class))
                .collect(Collectors.toMap(Annotation::annotationType, Function.identity()));
    }

    public static Collection<Class<? extends Annotation>> getRequiredQualifiers(AnnotatedElement element) {
        if (!element.isAnnotationPresent(RequireQualifiers.class))
            return Collections.emptyList();

        return Arrays.stream(element.getAnnotation(RequireQualifiers.class).value())
                .filter(annotationType -> annotationType.isAnnotationPresent(RimorQualifier.class))
                .collect(Collectors.toList());
    }

    private final Map<Class<? extends Annotation>, Annotation> qualifiers;
    private final Collection<Class<? extends Annotation>> requiredQualifiers;

    public QualifiedElement(Map<Class<? extends Annotation>, Annotation> qualifiers,
                            Collection<Class<? extends Annotation>> requiredQualifiers) {
        this.qualifiers = qualifiers;
        this.requiredQualifiers = requiredQualifiers;
    }

    @Override
    public Collection<Class<? extends Annotation>> getRequiredQualifiers() {
        return Collections.unmodifiableCollection(this.requiredQualifiers);
    }

    @Override
    public Map<Class<? extends Annotation>, Annotation> getQualifiersMap() {
        return Collections.unmodifiableMap(this.qualifiers);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof QualifiedElement)) return false;
        QualifiedElement that = (QualifiedElement) o;
        return Objects.equals(qualifiers, that.qualifiers) && Objects.equals(requiredQualifiers, that.requiredQualifiers);
    }

    @Override
    public int hashCode() {
        return Objects.hash(qualifiers, requiredQualifiers);
    }
}
