package st.networkers.rimor.command;

import st.networkers.rimor.internal.command.ResolvedCommand;

import java.util.HashMap;
import java.util.Map;

public class CommandRegistry {

    private final Map<String, ResolvedCommand> commands = new HashMap<>();

    public void registerCommand(ResolvedCommand command) {
        for (String alias : command.getAliases())
            this.commands.put(alias.toLowerCase(), command);
    }

    public ResolvedCommand getCommand(String alias) {
        return this.commands.get(alias.toLowerCase());
    }
}
