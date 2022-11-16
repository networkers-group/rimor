package st.networkers.rimor;

import st.networkers.rimor.command.AbstractCommandDefinition;
import st.networkers.rimor.command.Aliases;
import st.networkers.rimor.instruction.IgnoreMethodName;
import st.networkers.rimor.instruction.InstructionMapping;
import st.networkers.rimor.instruction.MainInstructionMapping;

import java.util.List;

@Aliases({"test", "testCommand"})
public class TestCommand extends AbstractCommandDefinition {

    public TestCommand() {
        registerSubcommand(new Bar());
    }

    @MainInstructionMapping
    public boolean main() {
        return true;
    }

    @InstructionMapping
    @Aliases("fooAlias")
    public void foo(@FooAnnotation List<String> params, String param0, String param1) {
    }

    @InstructionMapping
    @Aliases("bazAlias")
    @IgnoreMethodName
    public void baz() {
    }

    public static class Bar extends AbstractCommandDefinition {

        @InstructionMapping
        public boolean set(boolean enabled) {
            return enabled;
        }
    }
}
