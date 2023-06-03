package st.networkers.rimor.execute.task;

import st.networkers.rimor.context.ExecutionContext;
import st.networkers.rimor.executable.Executable;
import st.networkers.rimor.inject.Annotated;

/**
 * Represents a task to be automatically run along with every command execution. It can be annotated, so that it will
 * only be executed with commands or instructions that have those annotations.
 * <p>
 * This is useful, for example, to avoid repeating code such as permission checks (implementing a {@link PreExecutionTask})
 * or to save an object to the database after a command (implementing a {@link PostExecutionTask}).
 *
 * @see PreExecutionTask
 * @see PostExecutionTask
 * @see ExecutionTaskRegistry
 */
public interface ExecutionTask extends Annotated {

    void run(Executable executable, ExecutionContext context);

}
