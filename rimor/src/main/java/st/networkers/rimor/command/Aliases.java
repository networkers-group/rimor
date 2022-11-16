package st.networkers.rimor.command;

import st.networkers.rimor.instruction.IgnoreMethodName;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Contains the aliases of an {@link st.networkers.rimor.Executable}.
 * <p>
 * A command adds its definition class name as an alias if this annotation is not present.
 * <p>
 * An instruction also adds the method's name as an alias, if {@link IgnoreMethodName} is not present.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
public @interface Aliases {
    String[] value();
}
