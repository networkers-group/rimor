package st.networkers.rimor.qualify;

import java.lang.annotation.*;

/**
 * Marks an annotation to be a qualifier annotation, i.e. an annotation that is used to distinguish between
 * different uses for a parameter, a method, a class...
 *
 * <p>Using qualifier annotations is useful for:
 * <ul>
 *  <li>Differentiate between two instances of the same type in injection</li>
 *  <li>Define aspect pointcuts, if using rimor-aop</li>
 * </ul>
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.ANNOTATION_TYPE)
@Inherited
public @interface RimorQualifier {}
