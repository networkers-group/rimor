package st.networkers.rimor.execute;

import st.networkers.rimor.annotated.Annotated;
import st.networkers.rimor.aop.Advice;
import st.networkers.rimor.aop.AdviceRegistry;
import st.networkers.rimor.aop.ExceptionHandler;
import st.networkers.rimor.aop.exception.ExceptionHandlerRegistry;
import st.networkers.rimor.provide.ProviderRegistry;

/**
 * Represents anything involved in a command execution that is able to hold {@link ExceptionHandler}s
 * and {@link Advice}s.
 *
 * @see st.networkers.rimor.command.MappedCommand
 * @see st.networkers.rimor.instruction.Instruction
 */
public interface Executable extends Annotated {

    ExceptionHandlerRegistry getExceptionHandlerRegistry();

    AdviceRegistry getExecutionTaskRegistry();

    ProviderRegistry getProviderRegistry();

}
