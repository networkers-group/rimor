package st.networkers.rimor.instruction;

import st.networkers.rimor.context.ExecutionContextService;

import java.lang.reflect.Method;
import java.util.*;

/**
 * Class to resolve instruction handler methods.
 */
public class InstructionResolver {

    private final ExecutionContextService executionContextService;

    public InstructionResolver(ExecutionContextService executionContextService) {
        this.executionContextService = executionContextService;
    }

    public InstructionResolution resolveInstructions(Object bean) {
        InstructionResolution instructionResolution = new InstructionResolution();

        for (Method method : bean.getClass().getMethods()) {
            if (!method.isAnnotationPresent(MainInstructionMapping.class) && !method.isAnnotationPresent(InstructionMapping.class))
                continue;

            HandlerMethodInstruction instruction = this.resolveInstruction(bean, method);
            if (method.isAnnotationPresent(MainInstructionMapping.class))
                instructionResolution.setMainInstruction(instruction);

            if (method.isAnnotationPresent(InstructionMapping.class))
                instructionResolution.addInstruction(instruction);
        }
        return instructionResolution;
    }

    public HandlerMethodInstruction resolveInstruction(Object bean, Method method) {
        if (!method.isAnnotationPresent(InstructionMapping.class) && !method.isAnnotationPresent(MainInstructionMapping.class))
            throw new IllegalArgumentException("there is no InstructionMapping or MainInstructionMapping annotation " +
                                               "in " + method.getName());

        return HandlerMethodInstruction.builder()
                .setExecutionContextService(executionContextService)
                .bean(bean)
                .method(method)
                .identifiers(this.resolveIdentifiers(method))
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

    public static class InstructionResolution {
        private HandlerMethodInstruction mainInstruction = null;
        private final Collection<HandlerMethodInstruction> instructions = new ArrayList<>();

        public HandlerMethodInstruction getMainInstruction() {
            return mainInstruction;
        }

        public void setMainInstruction(HandlerMethodInstruction instruction) {
            if (this.mainInstruction != null)
                throw new IllegalArgumentException("trying to map multiple main instructions for " +
                                                   instruction.getBean().getClass());
            this.mainInstruction = instruction;
        }

        public Collection<HandlerMethodInstruction> getInstructions() {
            return instructions;
        }
        public void addInstruction(HandlerMethodInstruction instruction) {
            this.instructions.add(instruction);
        }

        public Map<String, Instruction> mapInstructionsByIdentifier() {
            Map<String, Instruction> instructions = new HashMap<>();
            this.instructions.forEach(instruction -> instruction.getIdentifiers().forEach(identifier -> {
                if (instructions.containsKey(identifier))
                    throw new IllegalArgumentException("trying to map multiple handler methods for the '" +
                                                       identifier + "' identifier in " + instruction.getBean().getClass());
                instructions.put(identifier, instruction);
            }));
            return instructions;
        }
    }
}
