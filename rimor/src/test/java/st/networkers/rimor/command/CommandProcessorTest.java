package st.networkers.rimor.command;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoSettings;
import st.networkers.rimor.bean.BeanProcessor;
import st.networkers.rimor.instruction.HandlerMethodInstruction;
import st.networkers.rimor.instruction.InstructionMapping;
import st.networkers.rimor.instruction.InstructionResolver;
import st.networkers.rimor.instruction.MainInstructionMapping;
import st.networkers.rimor.qualify.reflect.QualifiedMethod;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

@MockitoSettings
@SuppressWarnings("InnerClassMayBeStatic")
class CommandProcessorTest {

    @Mock CommandRegistry commandRegistry;
    @Mock BeanProcessor beanProcessor;
    CommandProcessor commandProcessor;

    @BeforeEach
    void beforeAll() {
        commandProcessor = spy(new CommandProcessor(beanProcessor, commandRegistry, new InstructionResolver(null)));
    }

    @Command({"foo", "bar"})
    static class TwoIdentifiersCommand {
    }

    @Test
    void whenResolvingCommand_identifiersAreFooAndBarInOrder() {
        MappedCommand command = commandProcessor.resolve(new TwoIdentifiersCommand(), false);
        assertThat(command.getIdentifiers()).containsExactly("foo", "bar");
    }

    @Command("")
    static class NoIdentifierCommand {}

    @Test
    void whenResolvingNoIdentifierCommand_throwsIllegalArgumentException() {
        assertThatThrownBy(() -> commandProcessor.resolve(new NoIdentifierCommand(), false)).isInstanceOf(IllegalArgumentException.class);
    }

    @Command("foo")
    static class CommandWithDefaultInstruction {
        @MainInstructionMapping
        public void defaultInstruction() {
        }
    }

    @Test
    void whenResolvingCommandWithDefaultInstruction_mainInstructionIsResolved() throws NoSuchMethodException {
        MappedCommand command = commandProcessor.resolve(new CommandWithDefaultInstruction(), false);
        assertThat(command.getMainInstruction())
                .map(instruction -> (HandlerMethodInstruction) instruction)
                .map(HandlerMethodInstruction::getQualifiedMethod)
                .map(QualifiedMethod::getMethod)
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
        MappedCommand command = commandProcessor.resolve(new CommandWithInstruction(), false);
        assertThat(command.getInstruction("bar"))
                .map(instruction -> (HandlerMethodInstruction) instruction)
                .map(HandlerMethodInstruction::getQualifiedMethod)
                .map(QualifiedMethod::getMethod)
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
        MappedCommand command = commandProcessor.resolve(new CommandWithDeclaredStaticSubcommand(), false);

        verify(commandProcessor).resolve(any(CommandWithDeclaredStaticSubcommand.BarSubcommand.class), eq(true));
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
        MappedCommand command = commandProcessor.resolve(new CommandWithDeclaredNonStaticSubcommand(), false);

        verify(commandProcessor).resolve(any(CommandWithDeclaredNonStaticSubcommand.BarSubcommand.class), eq(true));
        assertThat(command.getSubcommand("bar")).isPresent();
    }

    @Command("foo")
    static class CommandWithRegisteredInnerSubcommandDefinition extends AbstractCommandDefinition {
        public CommandWithRegisteredInnerSubcommandDefinition() {
            registerSubcommand(new BarSubcommand(0));
        }

        @Command("bar")
        public static class BarSubcommand {
            public BarSubcommand() {
                throw new IllegalStateException("this class should not be constructed automatically!");
            }

            private BarSubcommand(int i) {
            }
        }
    }

    @Test
    void whenResolvingCommandWithRegisteredSubcommand_barSubcommandIsResolvedAndRegistered() {
        MappedCommand command = commandProcessor.resolve(new CommandWithRegisteredInnerSubcommandDefinition(), false);

        verify(commandProcessor).resolve(any(CommandWithRegisteredInnerSubcommandDefinition.BarSubcommand.class), eq(true));
        assertThat(command.getSubcommand("bar")).isPresent();
    }

    @Test
    void whenResolvingCommandWithRegisteredInnerSubcommand_barSubcommandMustNotBeInstantiatedByProcessor() {
        assertThatCode(() -> {
            commandProcessor.resolveDeclaredSubcommands(new CommandWithRegisteredInnerSubcommandDefinition());
        }).doesNotThrowAnyException();
    }
}
