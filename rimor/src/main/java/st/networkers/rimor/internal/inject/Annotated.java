package st.networkers.rimor.internal.inject;

import java.lang.annotation.Annotation;
import java.util.Collection;

/**
 * Represents a dynamically annotated type.
 *
 * @see st.networkers.rimor.inject.Token
 * @see st.networkers.rimor.provide.RimorProvider
 * @see st.networkers.rimor.interpret.execution.ExecutionEnclosingTask
 * @see st.networkers.rimor.internal.instruction.Instruction
 */
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

    /**
     * The annotation instances of this {@link Annotated}.
     */
    Collection<Annotation> getAnnotations();

    /**
     * The classes of the required annotations of this {@link Annotated}.
     */
    Collection<Class<? extends Annotation>> getRequiredAnnotations();

    /**
     * Gets the annotation instance present in this {@link Annotated} for the given {@link Annotation} type.
     *
     * @param annotationClass class of the annotation to get
     * @return the annotation present in this {@link Annotated}, or null if not present.
     */
    <A extends Annotation> A getAnnotation(Class<A> annotationClass);

    /**
     * Whether this {@link Annotated} contains the given {@link Annotation}, or contains the given {@link Annotation}
     * class as a required annotation.
     *
     * @param annotation the annotation to check
     * @return if this {@link Annotated} contains the given {@link Annotation}
     */
    boolean isAnnotationPresent(Annotation annotation);

    /**
     * Whether this {@link Annotated} contains an instance of the given {@link Annotation} class, or contains the given
     * {@link Annotation} class as a required annotation
     *
     * @param annotationClass the annotation type to check
     * @return if this {@link Annotated} contains the given {@link Annotation}
     */
    boolean isAnnotationPresent(Class<? extends Annotation> annotationClass);
}
