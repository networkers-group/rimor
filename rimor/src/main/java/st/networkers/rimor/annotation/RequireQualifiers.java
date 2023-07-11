package st.networkers.rimor.annotation;

import java.lang.annotation.*;

/**
 * Declares that requires to have the provided qualifier annotation types.
 * <p>
 * This is useful for looking for annotation types rather than annotation instances: an element A qualifies for
 * another element B if every annotation instance of A is present in B or B requires the type of that annotation.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
public @interface RequireQualifiers {

    /**
     * The required annotation types.
     */
    Class<? extends Annotation>[] value();
}
