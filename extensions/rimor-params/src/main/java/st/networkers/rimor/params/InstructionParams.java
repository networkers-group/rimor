package st.networkers.rimor.params;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Attached to a {@code List<Object>} containing the parameters of a command execution. This does not contain the
 * path to the instruction.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.PARAMETER)
public @interface InstructionParams {
}
