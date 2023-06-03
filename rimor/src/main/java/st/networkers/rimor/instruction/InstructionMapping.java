package st.networkers.rimor.instruction;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation for mapping instructions onto handler methods.
 *
 * @see MainInstructionMapping
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface InstructionMapping {

    /**
     * The identifiers of the instruction. If empty, the method name will be used.
     */
    String[] value() default {};
}
