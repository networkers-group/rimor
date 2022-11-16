package st.networkers.rimor.params;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Attached to a list of objects (or, if the used framework like bukkit or jline do not parse anything, a list of
 * strings) containing the parameters of a command execution. This does not contain the path to the instruction.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.PARAMETER)
public @interface Params {
}
