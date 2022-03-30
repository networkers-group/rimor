package st.networkers.rimor;

import st.networkers.rimor.method.CommandMethod;
import st.networkers.rimor.method.SubcommandMethod;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class RimorCommand {
    private final Map<Set<String>, RimorCommand> subcommandGroups = new HashMap<>();
    private final Map<Set<String>, SubcommandMethod> subcommandMethods = new HashMap<>();
    private final Map<Set<String>, CommandMethod> commandMethod = new HashMap<>();
}
