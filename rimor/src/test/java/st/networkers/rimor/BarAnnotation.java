package st.networkers.rimor;

import st.networkers.rimor.qualify.RimorQualifier;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.PARAMETER, ElementType.METHOD})
@RimorQualifier
public @interface BarAnnotation {
}
