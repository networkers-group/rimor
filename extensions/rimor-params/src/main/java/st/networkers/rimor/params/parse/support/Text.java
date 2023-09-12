package st.networkers.rimor.params.parse.support;

import st.networkers.rimor.qualify.RimorQualifier;
import st.networkers.rimor.params.InstructionParam;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Indicates that a {@code String} {@link InstructionParam parameter} is a text parameter, i.e., the following command
 * parameters will be concatenated into a single String and this parameter won't only contain the first word of the text.
 *
 * <p>The {@link InstructionParam#index() parameter index} indicates the first parameter that counts as text. If the
 * number of parameters in an execution is less than the index, the whole text parameter will be {@code null} (instead
 * of an empty string).
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.PARAMETER)
@RimorQualifier
public @interface Text {
}
