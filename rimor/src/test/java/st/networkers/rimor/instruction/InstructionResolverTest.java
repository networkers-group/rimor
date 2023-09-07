package st.networkers.rimor.instruction;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import st.networkers.rimor.instruction.InstructionResolver.InstructionResolution;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class InstructionResolverTest {

    static InstructionResolver instructionResolver;

    @BeforeAll
    static void beforeAll() {
        instructionResolver = new InstructionResolver(null);
    }

    static class FooCommand {
        @MainInstructionMapping
        @InstructionMapping("help")
        public void defaultInstruction() {
        }

        @InstructionMapping({"bar", "barAlias"})
        public void barInstruction() {
        }

        @InstructionMapping
        public void baz() {
        }
    }

    @Test
    void whenResolvingBarInstruction_identifiersAreBarAndBarAlias() throws NoSuchMethodException {
        HandlerMethodInstruction instruction = instructionResolver.resolveInstruction(new FooCommand(), FooCommand.class.getMethod("barInstruction"));
        assertThat(instruction.getIdentifiers()).containsExactlyInAnyOrder("bar", "barAlias");
    }

    @Test
    void whenResolvingBazInstruction_identifiersAreMethodName() throws NoSuchMethodException {
        Instruction instruction = instructionResolver.resolveInstruction(new FooCommand(), FooCommand.class.getMethod("baz"));
        assertThat(instruction.getIdentifiers()).containsExactlyInAnyOrder("baz");
    }

    @Test
    void whenResolvingFooCommandInstructions_instructionsAreResolved() throws NoSuchMethodException {
        InstructionResolution instructions = instructionResolver.resolveInstructions(new FooCommand());
        assertThat(instructions.getMainInstruction())
                .isNotNull()
                .extracting(instruction -> instruction.getMethod().getMethod())
                .isEqualTo(FooCommand.class.getMethod("defaultInstruction"));
        assertThat(instructions.getInstructions()).hasSize(3);
    }

    static class CommandWithTwoDefaultInstructions {
        @MainInstructionMapping
        public void defaultInstruction() {
        }

        @MainInstructionMapping
        public void anotherDefaultInstruction() {
        }
    }

    @Test
    void whenResolvingCommandWithTwoDefaultInstructions_throwsIllegalArgumentException() {
        assertThatThrownBy(() -> instructionResolver.resolveInstructions(new CommandWithTwoDefaultInstructions()))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("trying to map multiple main instructions");
    }
}
