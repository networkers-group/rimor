package st.networkers.rimor.qualify;

import java.lang.annotation.Annotation;
import java.util.*;

/**
 * Represents a type that has {@link RimorQualifier qualifier} annotation instances and types.
 */
public interface Qualified {

    Map<Class<? extends Annotation>, Annotation> getQualifiersMap();

    /**
     * The required qualifier types of this element.
     */
    Collection<Class<? extends Annotation>> getRequiredQualifiers();

    /**
     * The qualifiers of this element.
     */
    default Collection<Annotation> getQualifiers() {
        return this.getQualifiersMap().values();
    }

    /**
     * Gets the qualifier instance present in this {@code Qualified} for the given {@link Annotation} type.
     *
     * @param annotationClass class of the annotation to get
     * @return the annotation present in this {@code Qualified}, or {@code null} if not present.
     */
    @SuppressWarnings("unchecked")
    default <A extends Annotation> A getQualifier(Class<A> annotationClass) {
        return (A) this.getQualifiersMap().get(annotationClass);
    }

    /**
     * Whether this {@code Qualified} contains the given qualifier {@link Annotation} instance, so that both qualifiers are
     * {@link Annotation#equals(Object) equal}.
     *
     * @param annotation the annotation to check
     * @return if this {@code Qualified} contains the given {@link Annotation}
     */
    default boolean isQualifierPresent(Annotation annotation) {
        return this.getQualifiers().contains(annotation);
    }

    /**
     * Whether this {@code Qualified} contains an instance of the given qualifier {@link Annotation} type, or contains
     * the given {@link Annotation} type as a required qualifier
     *
     * @param annotationClass the qualifier type to check
     * @return if this {@code Qualified} contains the given {@link Annotation}
     */
    default boolean isQualifierPresent(Class<? extends Annotation> annotationClass) {
        return this.getQualifiersMap().containsKey(annotationClass) || this.getRequiredQualifiers().contains(annotationClass);
    }

    /**
     * Checks whether this {@code Qualified} is assignable from given {@code Qualified}, i.e. if this {@code Qualified}
     * contains all the qualifier types and the same qualifier instances of the specified {@code Qualified}.
     *
     * <p>The qualifier types of an {@code Qualified} are composed of the types of its qualifier instances + its
     * required qualifier types.
     *
     * @param other          the {@code Qualified} be assigned to {@code this}
     * @param assignCriteria whether to check if {@code this} just contains all the qualifier types of {@code other},
     *                       or if both have the exact same qualifier types
     * @return {@code true} if this {@code Qualified} matches the qualifiers of {@code other}, {@code false} otherwise
     */
    default boolean containsAllAnnotationsOf(Qualified other, AssignCriteria assignCriteria) {
        return this.containsAllAnnotationTypesOf(other, assignCriteria) && this.containsAllAnnotationInstancesOf(other);
    }

    /**
     * Checks whether this {@code Qualified} contains all the qualifier types of the given {@code Qualified}.
     *
     * <p>The qualifier types of an {@code Qualified} are composed of the types of its qualifier instances + its
     * required qualifier types.
     *
     * @param other          the {@code Qualified} to check whether this {@code Qualified} matches its qualifier types
     * @param assignCriteria whether to check if {@code this} just contains all the qualifier types of {@code other},
     *                       or if both have the exact same qualifier types
     * @return {@code true} if this {@code Qualified} matches the qualifier types of {@code other}, {@code false} otherwise
     */
    default boolean containsAllAnnotationTypesOf(Qualified other, AssignCriteria assignCriteria) {
        Set<Class<? extends Annotation>> thisAnnotationTypes = new HashSet<>(this.getRequiredQualifiers());
        thisAnnotationTypes.addAll(this.getQualifiersMap().keySet());

        Set<Class<? extends Annotation>> otherAnnotationTypes = new HashSet<>(other.getRequiredQualifiers());
        otherAnnotationTypes.addAll(other.getQualifiersMap().keySet());

        return assignCriteria == AssignCriteria.CONTAINS
                ? thisAnnotationTypes.containsAll(otherAnnotationTypes)
                : thisAnnotationTypes.equals(otherAnnotationTypes);
    }

    /**
     * Checks whether this {@code Qualified} contains all the qualifier instances of the given {@code Qualified},
     * such that every qualifier of the provided {@code Qualified} matches {@link Annotation#equals(Object)} with one
     * of this {@code Qualified}.
     *
     * @param other the {@code Qualified} to check whether this {@code Qualified} matches its qualifier instances
     * @return {@code true} if this {@code Qualified} matches the qualifier instances of {@code other}, {@code false} otherwise
     */
    default boolean containsAllAnnotationInstancesOf(Qualified other) {
        Set<Annotation> thisAnnotations = new HashSet<>(this.getQualifiers());
        return thisAnnotations.containsAll(other.getQualifiers());
    }

    enum AssignCriteria {CONTAINS, EQUALS}
}
