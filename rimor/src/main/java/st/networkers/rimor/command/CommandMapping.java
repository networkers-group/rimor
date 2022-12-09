package st.networkers.rimor.command;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation for mapping commands onto {@link RimorCommand} implementations.
 * <p>
 * If this annotation is not present, the name of the command class is used as an alias for the command.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface CommandMapping {

    /**
     * The aliases of the command.
     */
    String[] value();
}
