package st.networkers.rimor;

import st.networkers.rimor.context.ExecutionContext;
import st.networkers.rimor.executable.Executable;
import st.networkers.rimor.execute.task.AbstractPreExecutionTask;

@FooAnnotation("")
public class FooAnnotationContainsValue extends AbstractPreExecutionTask {

    @Override
    public void run(Executable executable, ExecutionContext context) {
        throw new IllegalArgumentException("FooAnnotation is present!");
    }
}
