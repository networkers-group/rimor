package st.networkers.rimor;

import st.networkers.rimor.aop.Advice;
import st.networkers.rimor.aop.task.AbstractAdvice;
import st.networkers.rimor.execute.Executable;
import st.networkers.rimor.inject.ExecutionContext;

@FooAnnotation("")
public class FooAnnotationContainsValue extends AbstractAdvice implements Advice {

    @Override
    public void run(Executable executable, ExecutionContext context) {
        throw new IllegalArgumentException("FooAnnotation is present!");
    }
}
