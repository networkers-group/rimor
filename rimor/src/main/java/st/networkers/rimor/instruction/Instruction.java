package st.networkers.rimor.instruction;

import st.networkers.rimor.annotated.Annotated;
import st.networkers.rimor.annotated.AnnotatedProperties;
import st.networkers.rimor.aop.AdviceRegistry;
import st.networkers.rimor.aop.exception.ExceptionHandlerRegistry;
import st.networkers.rimor.execute.Executable;
import st.networkers.rimor.execute.ExecutableScope;
import st.networkers.rimor.provide.ProviderRegistry;

import java.util.Collection;
import java.util.Objects;

public class Instruction implements Annotated, Executable {

    private final MappingFunction mappingFunction;
    private final AnnotatedProperties annotatedProperties;
    private final ExecutableScope executableScope;
    private final Collection<String> identifiers;

    public Instruction(MappingFunction mappingFunction,
                       AnnotatedProperties annotatedProperties,
                       ExecutableScope executableScope,
                       Collection<String> identifiers) {
        this.mappingFunction = mappingFunction;
        this.annotatedProperties = annotatedProperties;
        this.executableScope = executableScope;
        this.identifiers = identifiers;
    }

    public MappingFunction getMappingFunction() {
        return mappingFunction;
    }

    @Override
    public AnnotatedProperties getAnnotatedProperties() {
        return annotatedProperties;
    }

    @Override
    public ExceptionHandlerRegistry getExceptionHandlerRegistry() {
        return executableScope.getExceptionHandlerRegistry();
    }

    @Override
    public AdviceRegistry getExecutionTaskRegistry() {
        return executableScope.getExecutionTaskRegistry();
    }

    @Override
    public ProviderRegistry getProviderRegistry() {
        return executableScope.getProviderRegistry();
    }

    public Collection<String> getIdentifiers() {
        return identifiers;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Instruction)) return false;
        Instruction that = (Instruction) o;
        return Objects.equals(mappingFunction, that.mappingFunction) && Objects.equals(annotatedProperties, that.annotatedProperties) && Objects.equals(executableScope, that.executableScope) && Objects.equals(identifiers, that.identifiers);
    }

    @Override
    public int hashCode() {
        return Objects.hash(mappingFunction, annotatedProperties, executableScope, identifiers);
    }
}
