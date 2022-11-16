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
     * The aliases of the instruction.
     */
    String[] value() default {};

    /**
     * Whether the method's name should be discarded as an alias. {@code false} by default.
     */
    boolean ignoreMethodName() default false;
}
