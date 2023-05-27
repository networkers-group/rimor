package st.networkers.rimor;

import st.networkers.rimor.context.ExecutionContext;
import st.networkers.rimor.execute.task.AbstractPreExecutionTask;
import st.networkers.rimor.inject.Token;

public class PresentBarAnnotationExecutionTask extends AbstractPreExecutionTask {

    public PresentBarAnnotationExecutionTask() {
        this.annotatedWith(BarAnnotation.class);
    }

    @Override
    public void run(Executable executable, ExecutionContext context) {
        if (context.get(new Token<>(String.class).annotatedWith(BarAnnotation.class)).isPresent())
            throw new IllegalStateException("BarAnnotation is present and a String with that annotation is also " +
                                            "present in the context!");
    }
}
