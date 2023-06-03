package st.networkers.rimor.executable;

import st.networkers.rimor.annotated.Annotated;
import st.networkers.rimor.execute.exception.ExceptionHandler;
import st.networkers.rimor.execute.exception.ExceptionHandlerRegistry;
import st.networkers.rimor.execute.task.ExecutionTask;
import st.networkers.rimor.execute.task.ExecutionTaskRegistry;
import st.networkers.rimor.provide.ProviderRegistry;

/**
 * Represents anything involved in a command execution that is able to hold {@link ExceptionHandler}s
 * and {@link ExecutionTask}s.
 *
 * @see st.networkers.rimor.command.MappedCommand
 * @see st.networkers.rimor.instruction.Instruction
 */
public interface Executable extends Annotated {

    ExceptionHandlerRegistry getExceptionHandlerRegistry();

    ExecutionTaskRegistry getExecutionTaskRegistry();

    ProviderRegistry getProviderRegistry();

}
