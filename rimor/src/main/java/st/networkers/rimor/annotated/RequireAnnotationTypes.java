package st.networkers.rimor.annotated;

import java.lang.annotation.*;

/**
 * Declares that an {@link Annotated} requires to have the provided qualifier annotation types.
 * <p>
 * This is useful for looking for annotation types rather than annotation instances: an {@link Annotated} A qualifies for
 * another {@link Annotated} B if every annotation instance of A is present in B or B requires the type of that annotation.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
public @interface RequireAnnotationTypes {

    /**
     * The required annotation types.
     */
    Class<? extends Annotation>[] value();
}
