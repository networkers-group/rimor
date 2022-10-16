package st.networkers.rimor.internal;

import st.networkers.rimor.internal.command.ResolvedCommand;

import java.util.HashMap;
import java.util.Map;

public class CommandRegistry {

    private final Map<String, ResolvedCommand> commands = new HashMap<>();

    public void registerCommand(ResolvedCommand resolvedCommand) {
        for (String alias : resolvedCommand.getAliases())
            this.commands.put(alias.toLowerCase(), resolvedCommand);
    }

    public ResolvedCommand getCommand(String alias) {
        return this.commands.get(alias.toLowerCase());
    }
}
