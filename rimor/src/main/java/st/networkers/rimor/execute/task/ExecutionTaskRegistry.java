package st.networkers.rimor.execute.task;

import st.networkers.rimor.context.ExecutionContext;
import st.networkers.rimor.executable.Executable;

import java.util.ArrayList;
import java.util.Collection;

public class ExecutionTaskRegistry implements Cloneable {

    private Collection<PreExecutionTask> preExecutionTasks;
    private Collection<PostExecutionTask> postExecutionTasks;

    public ExecutionTaskRegistry() {
        this(new ArrayList<>(), new ArrayList<>());
    }

    public ExecutionTaskRegistry(Collection<PreExecutionTask> preExecutionTasks, Collection<PostExecutionTask> postExecutionTasks) {
        this.preExecutionTasks = preExecutionTasks;
        this.postExecutionTasks = postExecutionTasks;
    }

    public void registerPreExecutionTask(PreExecutionTask task) {
        this.preExecutionTasks.add(task);
    }

    public void registerPostExecutionTask(PostExecutionTask task) {
        this.postExecutionTasks.add(task);
    }

    public void runPreExecutionTasks(Executable executable, ExecutionContext context) {
        this.runExecutionTasks(executable, context, this.preExecutionTasks);
    }

    public void runPostExecutionTasks(Executable executable, ExecutionContext context) {
        this.runExecutionTasks(executable, context, this.postExecutionTasks);
    }

    public Collection<PreExecutionTask> getPreExecutionTasks() {
        return this.preExecutionTasks;
    }

    public Collection<PostExecutionTask> getPostExecutionTasks() {
        return this.postExecutionTasks;
    }

    private void runExecutionTasks(Executable executable, ExecutionContext context, Collection<? extends ExecutionTask> executionTasks) {
        executionTasks.forEach(executionTask -> {
            if (executable.matchesAnnotations(executionTask))
                executionTask.run(executable, context);
        });
    }

    @Override
    public ExecutionTaskRegistry clone() {
        try {
            ExecutionTaskRegistry clone = (ExecutionTaskRegistry) super.clone();
            clone.preExecutionTasks = new ArrayList<>(this.preExecutionTasks);
            clone.postExecutionTasks = new ArrayList<>(this.postExecutionTasks);
            return clone;
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }
}
