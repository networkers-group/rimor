package st.networkers.rimor.context.provide;

import st.networkers.rimor.bean.RimorConfiguration;
import st.networkers.rimor.instruction.InstructionMapping;
import st.networkers.rimor.qualify.RequireQualifiers;
import st.networkers.rimor.qualify.RimorQualifier;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Declares a method to be an execution context provider.
 *
 * <p>The {@code @ProvidesContext} method can receive any execution
 * context as a parameter (like an {@link InstructionMapping instruction handler} method).
 *
 * <p>Any {@link #value() specified type} with the {@link RequireQualifiers qualifier types} and
 * {@link RimorQualifier qualifier annotations} present in this method will be provided by this execution context
 * provider handler method.
 *
 * <p><b>NOTE:</b> To register globally to work with all Rimor beans, the class that holds this method must be annotated
 * with {@link RimorConfiguration}. Otherwise, the provider will only be registered for the Rimor bean that holds this method.
 *
 * <p>For example, assuming that an integer qualified with @SenderId is always present in all execution contexts:
 * <pre>
 * &#64;RimorConfiguration
 * public class GlobalContextProviders {
 *
 *     &#64;ProvidesContext
 *     &#64;SenderAuthority
 *     public Authority provideAuthority(@SenderId int userId) {
 *         return authorityRepository.findByUserId(userId);
 *     }
 * }
 * </pre>
 * Then, Authority can be injected in any instruction handler method:
 * <pre>
 * &#64;Command("myCommand")
 * public class MyCommand {
 *
 *     &#64;InstructionMapping("doSomethingDangerous")
 *     public void doSomethingDangerous(&#64;SenderAuthority Authority userAuthority, ...) {
 *         // use userAuthority to check permissions
 *     }
 * }
 * </pre>
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface ProvidesContext {

    /**
     * The types that this method provides as context. If empty, the method's return type will be used.
     */
    Class<?>[] value() default {};
}
