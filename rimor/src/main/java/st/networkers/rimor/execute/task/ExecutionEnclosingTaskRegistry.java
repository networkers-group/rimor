package st.networkers.rimor.execute.task;

import java.util.Collection;

public interface ExecutionEnclosingTaskRegistry {

    void registerPreExecutionTask(PreExecutionTask task);

    void registerPostExecutionTask(PostExecutionTask task);

    Collection<PreExecutionTask> getPreExecutionTasks();

    Collection<PostExecutionTask> getPostExecutionTasks();
}
