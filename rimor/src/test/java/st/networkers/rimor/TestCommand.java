package st.networkers.rimor;

import st.networkers.rimor.command.Aliases;
import st.networkers.rimor.command.Command;
import st.networkers.rimor.instruction.IgnoreMethodName;
import st.networkers.rimor.instruction.Instruction;
import st.networkers.rimor.instruction.MainInstruction;
import st.networkers.rimor.provide.builtin.Param;
import st.networkers.rimor.provide.builtin.Params;

import java.util.List;

@Aliases({"test", "testCommand"})
public class TestCommand extends Command {

    public TestCommand() {
        registerSubcommand(new Bar());
    }

    @MainInstruction
    public boolean main() {
        return true;
    }

    @MainInstruction
    public boolean main(@Params List<String> params) {
        return true;
    }

    @Instruction
    @Aliases("fooAlias")
    public void foo(@Params List<String> params, @Param(0) String param0, @Param(1) String param1) {
    }

    @Instruction
    @Aliases("bazAlias")
    @IgnoreMethodName
    public void baz() {
    }

    public static class Bar extends Command {

        @Instruction
        public boolean set(@Param(0) boolean enabled) {
            return enabled;
        }
    }
}
