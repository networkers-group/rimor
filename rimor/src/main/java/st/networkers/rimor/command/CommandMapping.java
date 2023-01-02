package st.networkers.rimor.command;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation for mapping commands onto {@link RimorCommand} implementations.
 * <p>
 * If this annotation is not present, the class name is used for the command name.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface CommandMapping {

    /**
     * The name of the command. Must be unique. If empty (default), the first item of {@link #value()} is chosen.
     */
    String name() default "";

    /**
     * The aliases of the command.
     */
    String[] value();
}
