package st.networkers.rimor.instruction;

import st.networkers.rimor.execute.ExecutableScope;

import java.lang.reflect.Method;
import java.util.Collection;

public class InstructionBuilder {

    private Object commandInstance;
    private Method method;
    private ExecutableScope executableScope;
    private Collection<String> identifiers;

    public InstructionBuilder setCommandInstance(Object commandInstance) {
        this.commandInstance = commandInstance;
        return this;
    }

    public InstructionBuilder setMethod(Method method) {
        this.method = method;
        return this;
    }

    public InstructionBuilder setExecutableProperties(ExecutableScope executableScope) {
        this.executableScope = executableScope;
        return this;
    }

    public InstructionBuilder setIdentifiers(Collection<String> identifiers) {
        this.identifiers = identifiers;
        return this;
    }

    public Instruction create() {
        return new Instruction(commandInstance, method, executableScope, identifiers);
    }
}
