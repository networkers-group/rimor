package st.networkers.rimor.aop;

import st.networkers.rimor.inject.ExecutionContext;
import st.networkers.rimor.instruction.Instruction;

import java.util.Collection;

public interface ExceptionHandler<T extends Throwable> {

    Collection<Class<? extends T>> getHandledTypes();

    void handle(Instruction instruction, ExecutionContext executionContext, T throwable);

}
