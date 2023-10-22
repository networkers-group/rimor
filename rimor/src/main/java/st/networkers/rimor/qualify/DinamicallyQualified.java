package st.networkers.rimor.qualify;

import st.networkers.rimor.qualify.reflect.QualifiedElement;

import java.lang.annotation.Annotation;
import java.util.*;

/**
 * Abstract class to allow adding {@link RimorQualifier qualifier annotations} to elements at runtime.
 */
public abstract class DinamicallyQualified<T extends DinamicallyQualified<T>> implements Qualified {

    protected final Map<Class<? extends Annotation>, Annotation> qualifiers;
    protected final Collection<Class<? extends Annotation>> requiredQualifiers;

    protected DinamicallyQualified() {
        this(new HashMap<>(), new ArrayList<>());
    }

    protected DinamicallyQualified(Map<Class<? extends Annotation>, Annotation> qualifiers,
                                   Collection<Class<? extends Annotation>> requiredQualifiers) {
        this.qualifiers = qualifiers;
        this.requiredQualifiers = requiredQualifiers;
    }

    /**
     * Adds the given qualifier instance to this element.
     * This method differs from {@link #addQualifier(Annotation)} in that it returns {@code this} same element to call
     * more methods in chain, as if it was a builder.
     *
     * @param qualifierAnnotation the qualifier instance to add
     */
    public T qualifiedWith(Annotation qualifierAnnotation) {
        this.addQualifier(qualifierAnnotation);
        return this.casted();
    }

    /**
     * Adds the given qualifier instance to this element.
     *
     * @param qualifierAnnotation the qualifier instance to add
     */
    public void addQualifier(Annotation qualifierAnnotation) {
        Qualified.checkQualifierAnnotation(qualifierAnnotation);
        this.qualifiers.put(qualifierAnnotation.annotationType(), qualifierAnnotation);
    }

    /**
     * Adds the given required qualifier type to this element. This has the same effect as annotating a qualified element
     * with {@link RequireQualifiers}.
     * This method differs from {@link #addQualifier(Annotation)} in that it returns {@code this} same element to call
     * more methods in chain, as if it was a builder.
     *
     * <p>Check {@link RequireQualifiers} for more information on required qualifier types.
     *
     * @param qualifierType the required qualifier type to add
     */
    public T qualifiedWith(Class<? extends Annotation> qualifierType) {
        this.addRequiredQualifier(qualifierType);
        return this.casted();
    }

    /**
     * Adds the given required qualifier type to this element. This has the same effect as annotating a qualified element
     * with {@link RequireQualifiers}.
     *
     * <p>Check {@link RequireQualifiers} for more information on required qualifier types.
     *
     * @param qualifierType the required qualifier type to add
     */
    public void addRequiredQualifier(Class<? extends Annotation> qualifierType) {
        Qualified.checkQualifierAnnotation(qualifierType);
        this.requiredQualifiers.add(qualifierType);
    }

    public T withQualifiers(Map<Class<? extends Annotation>, Annotation> qualifiers) {
        this.addQualifiers(qualifiers);
        return this.casted();
    }

    public void addQualifiers(Map<Class<? extends Annotation>, Annotation> qualifiers) {
        this.qualifiers.putAll(qualifiers);
    }

    public T withRequiredQualifiers(Collection<Class<? extends Annotation>> qualifierTypes) {
        this.addRequiredQualifiers(qualifierTypes);
        return this.casted();
    }

    public void addRequiredQualifiers(Collection<Class<? extends Annotation>> qualifierTypes) {
        this.requiredQualifiers.addAll(qualifierTypes);
    }

    public T withQualifiersOf(QualifiedElement element) {
        this.addQualifiersOf(element);
        return this.casted();
    }

    public void addQualifiersOf(QualifiedElement element) {
        this.addQualifiers(element.getQualifiersMap());
        this.addRequiredQualifiers(element.getRequiredQualifiers());
    }

    @Override
    public Map<Class<? extends Annotation>, Annotation> getQualifiersMap() {
        return qualifiers;
    }

    @Override
    public Collection<Class<? extends Annotation>> getRequiredQualifiers() {
        return requiredQualifiers;
    }

    @SuppressWarnings("unchecked")
    private T casted() {
        return (T) this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DinamicallyQualified)) return false;
        DinamicallyQualified<?> that = (DinamicallyQualified<?>) o;
        return Objects.equals(qualifiers, that.qualifiers) && Objects.equals(requiredQualifiers, that.requiredQualifiers);
    }

    @Override
    public int hashCode() {
        return Objects.hash(qualifiers, requiredQualifiers);
    }
}
