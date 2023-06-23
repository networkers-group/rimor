package st.networkers.rimor.aop;

import st.networkers.rimor.annotated.Annotated;
import st.networkers.rimor.inject.ExecutionContext;
import st.networkers.rimor.instruction.Instruction;

/**
 * Represents advice to be run along with every instruction execution. In Rimor, pointcuts are only annotation matching,
 * so the advice will be executed with every instruction that matches its annotations.
 * <p>
 * Instructions inherit their command annotations, and commands also inherit their parent command annotations.
 *
 * @see AdviceRegistry
 */
public interface Advice extends Annotated {

    void run(Instruction instruction, ExecutionContext context);

}
