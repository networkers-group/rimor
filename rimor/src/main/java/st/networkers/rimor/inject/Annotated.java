package st.networkers.rimor.inject;

import st.networkers.rimor.command.MappedCommand;

import java.lang.annotation.Annotation;
import java.util.Collection;

/**
 * Represents a type to be injected based on its annotations.
 *
 * @see st.networkers.rimor.inject.Token
 * @see st.networkers.rimor.provide.RimorProvider
 * @see st.networkers.rimor.execute.task.ExecutionTask
 * @see st.networkers.rimor.instruction.Instruction
 * @see MappedCommand
 */
public interface Annotated {

    /**
     * The properties of this {@link Annotated}
     */
    AnnotatedProperties getAnnotatedProperties();

    /**
     * The annotations of this {@link Annotated}.
     */
    default Collection<Annotation> getAnnotations() {
        return this.getAnnotatedProperties().getAnnotations().values();
    }

    /**
     * The required annotation types of this {@link Annotated}.
     */
    default Collection<Class<? extends Annotation>> getRequiredAnnotations() {
        return this.getAnnotatedProperties().getRequiredAnnotations();
    }

    /**
     * Gets the annotation instance present in this {@link Annotated} for the given {@link Annotation} type.
     *
     * @param annotationClass class of the annotation to get
     * @return the annotation present in this {@link Annotated}, or null if not present.
     */
    @SuppressWarnings("unchecked")
    default <A extends Annotation> A getAnnotation(Class<A> annotationClass) {
        return (A) this.getAnnotatedProperties().getAnnotations().get(annotationClass);
    }

    /**
     * Whether this {@link Annotated} contains the given {@link Annotation}, or contains the given {@link Annotation}
     * class as a required annotation.
     *
     * @param annotation the annotation to check
     * @return if this {@link Annotated} contains the given {@link Annotation}
     */
    default boolean isAnnotationPresent(Annotation annotation) {
        return this.getAnnotations().contains(annotation) || this.getRequiredAnnotations().contains(annotation.annotationType());
    }

    /**
     * Whether this {@link Annotated} contains an instance of the given {@link Annotation} class, or contains the given
     * {@link Annotation} class as a required annotation
     *
     * @param annotationClass the annotation type to check
     * @return if this {@link Annotated} contains the given {@link Annotation}
     */
    default boolean isAnnotationPresent(Class<? extends Annotation> annotationClass) {
        return this.getAnnotatedProperties().getAnnotations().containsKey(annotationClass) || this.getRequiredAnnotations().contains(annotationClass);
    }

    /**
     * Checks if this Annotated matches the annotations of the specified {@code Annotated} parameter, i.e. if this
     * {@link Annotated} has all the annotations and required annotations of the specified {@code Annotated} parameter.
     *
     * @param annotated the Annotated to check if this matches its annotations
     * @return {@code true} if this Annotated matches the annotations of the specified {@code Annotated} parameter,
     * {@code false} otherwise
     */
    default boolean matchesAnnotations(Annotated annotated) {
        for (Class<? extends Annotation> annotation : annotated.getRequiredAnnotations())
            if (!this.isAnnotationPresent(annotation))
                return false;

        for (Annotation annotation : annotated.getAnnotations())
            if (!this.isAnnotationPresent(annotation))
                return false;

        return true;
    }
}
