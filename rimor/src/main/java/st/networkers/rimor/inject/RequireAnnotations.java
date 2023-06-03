package st.networkers.rimor.inject;

import java.lang.annotation.*;

/**
 * Declares that an {@link Annotated} (tokens, instructions, commands, execution tasks...) requires the provided
 * annotation types.
 * <p>
 * This annotation makes {@link Annotated#matchesAnnotations(Annotated)} match the provided annotation types rather
 * than instances.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
public @interface RequireAnnotations {

    /**
     * The required annotation types.
     */
    Class<? extends Annotation>[] value();
}
