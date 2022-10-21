package st.networkers.rimor.params;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.PARAMETER)
public @interface Param {

    String value() default "";

    String description() default "";

    /**
     * The position of this parameter, or -1 to automatically detect by the method parameters order.
     */
    int position() default -1;
}
