package st.networkers.rimor.internal.execute;

import lombok.Data;
import lombok.EqualsAndHashCode;
import st.networkers.rimor.Executable;
import st.networkers.rimor.execute.task.ExecutionEnclosingTask;

/**
 * Wraps a {@link Throwable} thrown inside a {@link st.networkers.rimor.execute.task.ExecutionEnclosingTask}.
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class ExecutionEnclosingTaskException extends RuntimeException {
    private final Executable executable;
    private final ExecutionEnclosingTask executionEnclosingTask;
    private final Throwable cause;
}
