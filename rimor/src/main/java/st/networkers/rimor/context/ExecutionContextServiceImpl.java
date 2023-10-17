package st.networkers.rimor.context;

import st.networkers.rimor.context.provide.ExecutionContextProvider;
import st.networkers.rimor.context.provide.ExecutionContextProviderRegistry;
import st.networkers.rimor.qualify.reflect.QualifiedMethod;
import st.networkers.rimor.qualify.reflect.QualifiedParameter;
import st.networkers.rimor.util.OptionalUtils;
import st.networkers.rimor.util.ReflectionUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class ExecutionContextServiceImpl implements ExecutionContextService {

    private final ExecutionContextProviderRegistry globalProviderRegistry;
    private final Map<Object, ExecutionContextProviderRegistry> beanProviderRegistries = new HashMap<>();

    public ExecutionContextServiceImpl(ExecutionContextProviderRegistry globalProviderRegistry) {
        this.globalProviderRegistry = globalProviderRegistry;
    }

    @Override
    public <T> Optional<T> get(Token<T> token, ExecutionContext context) {
        return OptionalUtils.firstPresent(
                context.get(token),
                () -> this.fromProviderRegistry(globalProviderRegistry, token, context)
        );
    }

    @Override
    public <T> Optional<T> get(Token<T> token, Object bean, ExecutionContext context) {
        ExecutionContextProviderRegistry beanProviderRegistry = beanProviderRegistries.get(bean);
        if (beanProviderRegistry == null)
            return this.get(token, context);

        return OptionalUtils.firstPresent(
                context.get(token),
                () -> this.fromProviderRegistry(beanProviderRegistry, token, context),
                () -> this.fromProviderRegistry(globalProviderRegistry, token, context)
        );
    }

    public <T> Optional<T> fromProviderRegistry(ExecutionContextProviderRegistry executionContextProviderRegistry, Token<T> token, ExecutionContext context) {
        return executionContextProviderRegistry.findFor(token).map(provider -> provider.get(token, context));
    }

    @Override
    public void registerGlobalExecutionContextProvider(ExecutionContextProvider<?> executionContextProvider) {
        this.globalProviderRegistry.register(executionContextProvider);
    }

    @Override
    public void registerExecutionContextProvider(ExecutionContextProvider<?> executionContextProvider, Object bean) {
        this.beanProviderRegistries.computeIfAbsent(bean, b -> new ExecutionContextProviderRegistry())
                .register(executionContextProvider);
    }

    @Override
    public Object invokeMethod(QualifiedMethod method, Object bean, ExecutionContext context) {
        return ReflectionUtils.invoke(method.getMethod(), bean, resolveParameters(method, bean, context));
    }

    private Object[] resolveParameters(QualifiedMethod method, Object bean, ExecutionContext context) {
        Object[] parameters = new Object[method.getQualifiedParameters().size()];

        int i = 0;
        for (QualifiedParameter parameter : method.getQualifiedParameters()) {
            parameters[i++] = this.get(ParameterToken.build(method, parameter), bean, context).orElse(null);
        }

        return parameters;
    }
}
