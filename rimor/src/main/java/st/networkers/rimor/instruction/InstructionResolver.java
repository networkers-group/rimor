package st.networkers.rimor.instruction;

import st.networkers.rimor.execute.ExecutableScope;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class InstructionResolver {

    public ResolvedInstructions resolveInstructions(Object commandInstance, ExecutableScope executableScope) {
        ResolvedInstructions results = new ResolvedInstructions();

        for (Method method : commandInstance.getClass().getMethods()) {
            if (!method.isAnnotationPresent(MainInstructionMapping.class) && !method.isAnnotationPresent(InstructionMapping.class))
                continue;

            Instruction instruction = this.resolveInstruction(commandInstance, executableScope, method);
            if (method.isAnnotationPresent(MainInstructionMapping.class))
                results.setMainInstruction(instruction);

            if (method.isAnnotationPresent(InstructionMapping.class))
                results.addInstruction(instruction);
        }
        return results;
    }

    public Instruction resolveInstruction(Object commandInstance, ExecutableScope executableScope, Method method) {
        return new InstructionBuilder()
                .setCommandInstance(commandInstance)
                .setMethod(method)
                .setExecutableProperties(executableScope)
                .setIdentifiers(this.resolveIdentifiers(method))
                .create();
    }

    private List<String> resolveIdentifiers(Method method) {
        InstructionMapping instructionMapping = method.getAnnotation(InstructionMapping.class);
        if (instructionMapping == null)
            return Collections.emptyList();

        List<String> identifiers = new ArrayList<>(Arrays.asList(instructionMapping.value()));
        if (identifiers.isEmpty())
            identifiers.add(method.getName());

        return identifiers;
    }
}
