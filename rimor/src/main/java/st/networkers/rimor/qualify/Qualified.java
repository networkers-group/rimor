package st.networkers.rimor.qualify;

import java.lang.annotation.Annotation;
import java.util.*;

/**
 * Represents a type that has {@link RimorQualifier qualifier} annotation instances and types.
 */
public interface Qualified {

    Map<Class<? extends Annotation>, Annotation> getAnnotationsMap();

    /**
     * The required annotation types of this element.
     */
    Collection<Class<? extends Annotation>> getRequiredAnnotations();

    /**
     * The annotations of this element.
     */
    default Collection<Annotation> getAnnotations() {
        return this.getAnnotationsMap().values();
    }

    /**
     * Gets the annotation instance present in this {@code Annotated} for the given {@link Annotation} type.
     *
     * @param annotationClass class of the annotation to get
     * @return the annotation present in this {@code Annotated}, or {@code null} if not present.
     */
    @SuppressWarnings("unchecked")
    default <A extends Annotation> A getAnnotation(Class<A> annotationClass) {
        return (A) this.getAnnotationsMap().get(annotationClass);
    }

    /**
     * Whether this {@code Annotated} contains the given {@link Annotation} instance, so that both annotations are
     * {@link Annotation#equals(Object) equal}.
     *
     * @param annotation the annotation to check
     * @return if this {@code Annotated} contains the given {@link Annotation}
     */
    default boolean isAnnotationPresent(Annotation annotation) {
        return this.getAnnotations().contains(annotation);
    }

    /**
     * Whether this {@code Annotated} contains an instance of the given {@link Annotation} type, or contains the given
     * {@link Annotation} type as a required annotation
     *
     * @param annotationClass the annotation type to check
     * @return if this {@code Annotated} contains the given {@link Annotation}
     */
    default boolean isAnnotationPresent(Class<? extends Annotation> annotationClass) {
        return this.getAnnotationsMap().containsKey(annotationClass) || this.getRequiredAnnotations().contains(annotationClass);
    }

    /**
     * Checks whether this {@code Annotated} is assignable from given {@code Annotated}, i.e. if this {@code Annotated}
     * contains all the annotation types and the same annotation instances of the specified {@code Annotated}.
     *
     * <p>The annotation types of an {@code Annotated} are composed of the types of its annotation instances + its
     * required annotation types.
     *
     * @param other          the {@code Annotated} be assigned to {@code this}
     * @param assignCriteria whether to check if {@code this} just contains all the annotation types of {@code other},
     *                       or if both have the exact same annotation types
     * @return {@code true} if this {@code Annotated} matches the annotations of {@code other}, {@code false} otherwise
     */
    default boolean containsAllAnnotationsOf(Qualified other, AssignCriteria assignCriteria) {
        return this.containsAllAnnotationTypesOf(other, assignCriteria) && this.containsAllAnnotationInstancesOf(other);
    }

    /**
     * Checks whether this {@code Annotated} contains all the annotation types of the given {@code Annotated}.
     *
     * <p>The annotation types of an {@code Annotated} are composed of the types of its annotation instances + its
     * required annotation types.
     *
     * @param other          the {@code Annotated} to check whether this {@code Annotated} matches its annotation types
     * @param assignCriteria whether to check if {@code this} just contains all the annotation types of {@code other},
     *                       or if both have the exact same annotation types
     * @return {@code true} if this {@code Annotated} matches the annotation types of {@code other}, {@code false} otherwise
     */
    default boolean containsAllAnnotationTypesOf(Qualified other, AssignCriteria assignCriteria) {
        Set<Class<? extends Annotation>> thisAnnotationTypes = new HashSet<>(this.getRequiredAnnotations());
        thisAnnotationTypes.addAll(this.getAnnotationsMap().keySet());

        Set<Class<? extends Annotation>> otherAnnotationTypes = new HashSet<>(other.getRequiredAnnotations());
        otherAnnotationTypes.addAll(other.getAnnotationsMap().keySet());

        return assignCriteria == AssignCriteria.CONTAINS
                ? thisAnnotationTypes.containsAll(otherAnnotationTypes)
                : thisAnnotationTypes.equals(otherAnnotationTypes);
    }

    /**
     * Checks whether this {@code Annotated} contains all the annotation instances of the given {@code Annotated},
     * such that every annotation of the provided {@code Annotated} matches {@link Annotation#equals(Object)} with one
     * of this {@code Annotated}.
     *
     * @param other the {@code Annotated} to check whether this {@code Annotated} matches its annotation instances
     * @return {@code true} if this {@code Annotated} matches the annotation instances of {@code other}, {@code false} otherwise
     */
    default boolean containsAllAnnotationInstancesOf(Qualified other) {
        Set<Annotation> thisAnnotations = new HashSet<>(this.getAnnotations());
        return thisAnnotations.containsAll(other.getAnnotations());
    }

    enum AssignCriteria {CONTAINS, EQUALS}
}
