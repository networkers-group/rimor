package st.networkers.rimor.command;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class CommandRegistry {

    private final Map<String, MappedCommand> commands = new HashMap<>();

    public void registerCommand(MappedCommand command) {
        command.getIdentifiers().forEach(identifier -> this.commands.put(identifier.toLowerCase(), command));
    }

    public Collection<MappedCommand> getRegisteredCommands() {
        return this.commands.values();
    }

    public MappedCommand getCommand(String identifier) {
        return this.commands.get(identifier.toLowerCase());
    }
}
