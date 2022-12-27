package st.networkers.rimor.internal.execute.task;

import st.networkers.rimor.execute.task.ExecutionTaskRegistry;
import st.networkers.rimor.execute.task.PostExecutionTask;
import st.networkers.rimor.execute.task.PreExecutionTask;

import java.util.ArrayList;
import java.util.Collection;

public class ExecutionTaskRegistryImpl implements ExecutionTaskRegistry {

    private final Collection<PreExecutionTask> preExecutionTasks = new ArrayList<>();
    private final Collection<PostExecutionTask> postExecutionTasks = new ArrayList<>();

    @Override
    public void registerPreExecutionTask(PreExecutionTask task) {
        this.preExecutionTasks.add(task);
    }

    @Override
    public void registerPostExecutionTask(PostExecutionTask task) {
        this.postExecutionTasks.add(task);
    }

    @Override
    public Collection<PreExecutionTask> getPreExecutionTasks() {
        return this.preExecutionTasks;
    }

    @Override
    public Collection<PostExecutionTask> getPostExecutionTasks() {
        return this.postExecutionTasks;
    }
}
