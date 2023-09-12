package st.networkers.rimor.qualify;

import java.lang.annotation.*;

/**
 * Declares that requires having the provided {@link RimorQualifier qualifier} annotation types.
 * <p>
 * This is useful for looking for annotation types rather than annotation instances: an element A qualifies for
 * another element B if, for each annotation instance of A, one of the following is true:
 * <ul>
 *     <li>the annotation instance is present in B (the instances of A and B are equal)</li>
 *     <li>the annotation type is required by B</li>
 * </ul>
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
public @interface RequireQualifiers {

    /**
     * The required {@link RimorQualifier qualifier} annotation types.
     */
    Class<? extends Annotation>[] value();
}
