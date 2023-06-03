package st.networkers.rimor.instruction;

import st.networkers.rimor.annotated.Annotated;
import st.networkers.rimor.annotated.AnnotatedProperties;
import st.networkers.rimor.executable.Executable;
import st.networkers.rimor.executable.ExecutableProperties;
import st.networkers.rimor.execute.exception.ExceptionHandlerRegistry;
import st.networkers.rimor.execute.task.ExecutionTaskRegistry;
import st.networkers.rimor.provide.ProviderRegistry;
import st.networkers.rimor.reflect.CachedMethod;

import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Objects;

public class Instruction implements Annotated, Executable {

    private final Object commandInstance;
    private final CachedMethod method;

    private final AnnotatedProperties annotatedProperties;
    private final ExecutableProperties executableProperties;

    private final Collection<String> identifiers;

    public Instruction(Object commandInstance,
                       Method method,
                       ExecutableProperties executableProperties,
                       Collection<String> identifiers) {
        this.commandInstance = commandInstance;
        this.method = CachedMethod.build(method);
        this.annotatedProperties = AnnotatedProperties.build(method);
        this.executableProperties = executableProperties;
        this.identifiers = identifiers;
    }

    public Object getCommandInstance() {
        return commandInstance;
    }

    public CachedMethod getMethod() {
        return method;
    }

    @Override
    public AnnotatedProperties getAnnotatedProperties() {
        return annotatedProperties;
    }

    @Override
    public ExceptionHandlerRegistry getExceptionHandlerRegistry() {
        return executableProperties.getExceptionHandlerRegistry();
    }

    @Override
    public ExecutionTaskRegistry getExecutionTaskRegistry() {
        return executableProperties.getExecutionTaskRegistry();
    }

    @Override
    public ProviderRegistry getProviderRegistry() {
        return executableProperties.getProviderRegistry();
    }

    public Collection<String> getIdentifiers() {
        return identifiers;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Instruction)) return false;
        Instruction that = (Instruction) o;
        return Objects.equals(commandInstance, that.commandInstance) && Objects.equals(method, that.method) && Objects.equals(annotatedProperties, that.annotatedProperties) && Objects.equals(executableProperties, that.executableProperties) && Objects.equals(identifiers, that.identifiers);
    }

    @Override
    public int hashCode() {
        return Objects.hash(commandInstance, method, annotatedProperties, executableProperties, identifiers);
    }
}
