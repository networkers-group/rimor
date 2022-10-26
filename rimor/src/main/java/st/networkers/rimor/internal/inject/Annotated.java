package st.networkers.rimor.internal.inject;

import java.lang.annotation.Annotation;
import java.util.Collection;

public interface Annotated {

    boolean matchesAnnotations(Annotated annotated);

    Collection<Annotation> getAnnotations();

    Collection<Class<? extends Annotation>> getRequiredAnnotations();

    <A extends Annotation> A getAnnotation(Class<A> annotationClass);

    boolean isAnnotationPresent(Annotation annotation);

    boolean isAnnotationPresent(Class<? extends Annotation> annotationClass);

}
