package st.networkers.rimor.internal.inject;

import java.lang.annotation.Annotation;
import java.util.Collection;

public interface Annotated {

    /**
     * Checks if this Annotated matches the annotations of the specified {@code Annotated} parameter, i.e. if this
     * Annotated has the same annotations and the required annotations of the specified {@code Annotated} parameter.
     *
     * @param annotated the Annotated to check if this matches its annotations
     * @return {@code true} if this Annotated matches the annotations of the specified {@code Annotated} parameter,
     * {@code false} otherwise
     */
    boolean matchesAnnotations(Annotated annotated);

    Collection<Annotation> getAnnotations();

    Collection<Class<? extends Annotation>> getRequiredAnnotations();

    <A extends Annotation> A getAnnotation(Class<A> annotationClass);

    boolean isAnnotationPresent(Annotation annotation);

    boolean isAnnotationPresent(Class<? extends Annotation> annotationClass);

}
