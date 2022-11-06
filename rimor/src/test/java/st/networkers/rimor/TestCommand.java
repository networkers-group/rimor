package st.networkers.rimor;

import st.networkers.rimor.command.AbstractCommand;
import st.networkers.rimor.command.Aliases;
import st.networkers.rimor.instruction.IgnoreMethodName;
import st.networkers.rimor.instruction.Instruction;
import st.networkers.rimor.instruction.MainInstruction;

import java.util.List;

@Aliases({"test", "testCommand"})
public class TestCommand extends AbstractCommand {

    public TestCommand() {
        registerSubcommand(new Bar());
    }

    @MainInstruction
    public boolean main() {
        return true;
    }

    @Instruction
    @Aliases("fooAlias")
    public void foo(@FooAnnotation List<String> params, String param0, String param1) {
    }

    @Instruction
    @Aliases("bazAlias")
    @IgnoreMethodName
    public void baz() {
    }

    public static class Bar extends AbstractCommand {

        @Instruction
        public boolean set(boolean enabled) {
            return enabled;
        }
    }
}
