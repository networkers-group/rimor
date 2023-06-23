package st.networkers.rimor.instruction;

import st.networkers.rimor.inject.ExecutionContext;

public interface MappingFunction {

    Object call(Instruction instruction, ExecutionContext executionContext);

}
