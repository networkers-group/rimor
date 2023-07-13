package st.networkers.rimor.params;

import st.networkers.rimor.annotation.RimorQualifier;
import st.networkers.rimor.params.parse.InstructionParamParser;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Method parameters with this annotation are injected with the corresponding command parameter.
 *
 * <p>If the index of the command parameter is not manually specified in this annotation, it is automatically
 * detected by the position of this parameter relative to all the method's {@code @Param}-annotated parameters.
 *
 * <p>The injected object will be {@code null} if the index is greater than the command parameter size.
 *
 * <p>If the type of the method parameter matches the type of the command parameter, it is directly injected. Also,
 * the object can be parsed and injected into, for example, a Boolean or Enum parameter, following the registered {@link InstructionParamParser}s.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.PARAMETER)
@RimorQualifier
public @interface InstructionParam {

    /**
     * The name of the parameter.
     */
    String value() default "";

    /**
     * The description of the parameter.
     */
    String description() default "";

    /**
     * The index of this parameter, or -1 to automatically detect by the method's parameter order.
     */
    int index() default -1;
}
