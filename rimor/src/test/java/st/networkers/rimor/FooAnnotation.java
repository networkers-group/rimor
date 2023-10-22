package st.networkers.rimor;

import st.networkers.rimor.qualify.RimorQualifier;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.PARAMETER, ElementType.METHOD})
@RimorQualifier
public @interface FooAnnotation {
    String value();
}
