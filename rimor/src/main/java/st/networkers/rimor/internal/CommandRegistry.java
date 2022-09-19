package st.networkers.rimor.internal;

import st.networkers.rimor.internal.command.RimorCommand;

import java.util.HashMap;
import java.util.Map;

public class CommandRegistry {

    private final Map<String, RimorCommand> commands = new HashMap<>();

    public void registerCommand(RimorCommand rimorCommand) {
        for (String alias : rimorCommand.getAliases())
            this.commands.put(alias.toLowerCase(), rimorCommand);
    }

    public RimorCommand getCommand(String alias) {
        return this.commands.get(alias.toLowerCase());
    }
}
