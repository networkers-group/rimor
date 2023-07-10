package st.networkers.rimor.params.parse.support;

import st.networkers.rimor.params.InstructionParam;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Indicates that a {@code boolean} {@link InstructionParam parameter} should be parsed to {@code true}
 * if the command parameter matches one of the specified values.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.PARAMETER)
public @interface TrueValues {

    /**
     * The values to parse to {@code true}.
     */
    String[] value();
}
