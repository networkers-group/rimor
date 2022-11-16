package st.networkers.rimor.instruction;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Maps the method this annotation is attached to as an instruction.
 *
 * @see MainInstructionMapping
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface InstructionMapping {
}
