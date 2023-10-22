package st.networkers.rimor.qualify;

import java.lang.annotation.*;

/**
 * Declares that an element requires having the provided {@link RimorQualifier qualifier} types.
 * <p>
 * This is useful for looking for the same qualifier types rather than equal qualifier instances: an element A
 * is assignable to another element B if, for each qualifier instance of A, one of the following is true:
 * <ul>
 *     <li>the qualifier instance is present in B (both qualifier instances are equal)</li>
 *     <li>the qualifier type is required by B</li>
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
