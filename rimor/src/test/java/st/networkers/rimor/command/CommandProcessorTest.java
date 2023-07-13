package st.networkers.rimor.command;

import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoSettings;
import st.networkers.rimor.instruction.HandlerMethodInstruction;
import st.networkers.rimor.instruction.InstructionMapping;
import st.networkers.rimor.instruction.InstructionResolver;
import st.networkers.rimor.instruction.MainInstructionMapping;
import st.networkers.rimor.reflect.CachedMethod;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;

@MockitoSettings
@SuppressWarnings("InnerClassMayBeStatic")
class CommandProcessorTest {

    InstructionResolver instructionResolver = new InstructionResolver(null);

    @Mock CommandRegistry commandRegistry;
    @Spy CommandProcessor commandProcessor = new CommandProcessor(commandRegistry, instructionResolver);

    @Command({"foo", "bar"})
    static class TwoIdentifiersCommand {
    }

    @Test
    void whenResolvingCommand_identifiersAreFooAndBarInOrder() {
        MappedCommand command = commandProcessor.resolve(new TwoIdentifiersCommand());
        assertThat(command.getIdentifiers()).containsExactly("foo", "bar");
    }

    @Command("")
    static class NoIdentifierCommand {}

    @Test
    void whenResolvingNoIdentifierCommand_throwsIllegalArgumentException() {
        assertThatThrownBy(() -> commandProcessor.resolve(new NoIdentifierCommand())).isInstanceOf(IllegalArgumentException.class);
    }

    @Command("foo")
    static class CommandWithDefaultInstruction {
        @MainInstructionMapping
        public void defaultInstruction() {
        }
    }

    @Test
    void whenResolvingCommandWithDefaultInstruction_mainInstructionIsResolved() throws NoSuchMethodException {
        MappedCommand command = commandProcessor.resolve(new CommandWithDefaultInstruction());
        assertThat(command.getMainInstruction())
                .map(instruction -> (HandlerMethodInstruction) instruction)
                .map(HandlerMethodInstruction::getMethod)
                .map(CachedMethod::getMethod)
                .contains(CommandWithDefaultInstruction.class.getMethod("defaultInstruction"));
    }

    @Command("foo")
    static class CommandWithInstruction {
        @InstructionMapping("bar")
        public void barInstruction() {
        }
    }

    @Test
    void whenResolvingCommandWithInstruction_barInstructionIsResolved() throws NoSuchMethodException {
        MappedCommand command = commandProcessor.resolve(new CommandWithInstruction());
        assertThat(command.getInstruction("bar"))
                .map(instruction -> (HandlerMethodInstruction) instruction)
                .map(HandlerMethodInstruction::getMethod)
                .map(CachedMethod::getMethod)
                .contains(CommandWithInstruction.class.getMethod("barInstruction"));
    }

    @Command("foo")
    static class CommandWithDeclaredStaticSubcommand {
        @Command("bar")
        public static class BarSubcommand {
            @MainInstructionMapping
            public void defaultInstruction() {
            }
        }
    }

    @Test
    void whenResolvingCommandWithDeclaredStaticSubcommand_barSubcommandIsResolvedAndRegistered() {
        MappedCommand command = commandProcessor.resolve(new CommandWithDeclaredStaticSubcommand());

        verify(commandProcessor).resolve(any(CommandWithDeclaredStaticSubcommand.BarSubcommand.class));
        assertThat(command.getSubcommand("bar")).isPresent();
    }

    @Command("foo")
    static class CommandWithDeclaredNonStaticSubcommand {
        @Command("bar")
        public class BarSubcommand {
        }
    }

    @Test
    void whenResolvingCommandWithDeclaredNonStaticSubcommand_barSubcommandIsResolvedAndRegistered() {
        MappedCommand command = commandProcessor.resolve(new CommandWithDeclaredNonStaticSubcommand());

        verify(commandProcessor).resolve(any(CommandWithDeclaredNonStaticSubcommand.BarSubcommand.class));
        assertThat(command.getSubcommand("bar")).isPresent();
    }

    @Command("foo")
    static class CommandWithRegisteredInnerSubcommandDefinition extends AbstractCommandDefinition {
        public CommandWithRegisteredInnerSubcommandDefinition() {
            registerSubcommand(new BarSubcommand(0));
        }

        @Command("bar")
        static class BarSubcommand {
            private BarSubcommand(int i) {
            }
        }
    }

    @Test
    void whenResolvingCommandWithRegisteredSubcommand_barSubcommandIsResolvedAndRegistered() {
        MappedCommand command = commandProcessor.resolve(new CommandWithRegisteredInnerSubcommandDefinition());

        verify(commandProcessor).resolve(any(CommandWithRegisteredInnerSubcommandDefinition.BarSubcommand.class));
        assertThat(command.getSubcommand("bar")).isPresent();
    }
}
