package st.networkers.rimor.instruction;

import st.networkers.rimor.execute.Executable;
import st.networkers.rimor.inject.ExecutionContext;

import java.util.Collection;

/**
 * A command instruction.
 */
public interface Instruction extends Executable {

    Object run(ExecutionContext executionContext);

    Collection<String> getIdentifiers();

}
