package st.networkers.rimor.annotated;

import java.lang.annotation.*;

/**
 * Declares that an {@link Annotated} requires to have the provided annotation types.
 * <p>
 * This is useful for looking for annotation types rather than annotation instances: an {@link Annotated} A matches
 * another {@link Annotated} B if A has all the annotations of B <b>and</b> A has an instance for all the required
 * annotation types of B.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
public @interface RequireAnnotationTypes {

    /**
     * The required annotation types.
     */
    Class<? extends Annotation>[] value();
}
