package st.networkers.rimor.command;

import java.util.HashMap;
import java.util.Map;

public class CommandRegistry {

    private final Map<String, MappedCommand> commands = new HashMap<>();

    public void registerCommand(MappedCommand command) {
        for (String alias : command.getAliases())
            this.commands.put(alias.toLowerCase(), command);
    }

    public MappedCommand getCommand(String alias) {
        return this.commands.get(alias.toLowerCase());
    }
}
