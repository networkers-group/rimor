package st.networkers.rimor.params;

import st.networkers.rimor.inject.ExecutionContext;
import st.networkers.rimor.params.parse.ParamParser;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Instruction method parameters with this annotation are injected with the object from the params list annotated with {@link Params} present in the {@link ExecutionContext}.
 * <p>
 * The position of the parameter is automatically detected by the position of the method parameters if it is not
 * manually specified in this annotation.
 * <p>
 * The injected object will be {@code null} if the position is out of bounds (there are fewer parameters than expected).
 * <p>
 * The object can be parsed from, for example, a String, Boolean or Enum, following the registered {@link ParamParser}s.
 * If the type of the method parameter matches the type of the parameter in the list, it is directly injected.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.PARAMETER)
public @interface Param {

    /**
     * Name of the parameter
     */
    String value() default "";

    /**
     * Description of the parameter
     */
    String description() default "";

    /**
     * The position of this parameter, or -1 to automatically detect by the method parameters order.
     */
    int position() default -1;
}
