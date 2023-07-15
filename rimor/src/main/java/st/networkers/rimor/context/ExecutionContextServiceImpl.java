package st.networkers.rimor.context;

import st.networkers.rimor.bean.BeanManager;
import st.networkers.rimor.context.provide.ExecutionContextProviderRegistry;
import st.networkers.rimor.reflect.CachedMethod;
import st.networkers.rimor.reflect.CachedParameter;
import st.networkers.rimor.util.OptionalUtils;
import st.networkers.rimor.util.ReflectionUtils;

import java.util.Optional;

public class ExecutionContextServiceImpl implements ExecutionContextService {

    private final BeanManager beanManager;
    private final ExecutionContextProviderRegistry globalExecutionContextProviderRegistry;

    public ExecutionContextServiceImpl(BeanManager beanManager, ExecutionContextProviderRegistry globalExecutionContextProviderRegistry) {
        this.beanManager = beanManager;
        this.globalExecutionContextProviderRegistry = globalExecutionContextProviderRegistry;
    }

    @Override
    public <T> Optional<T> get(Token<T> token, ExecutionContext context) {
        return OptionalUtils.firstPresent(
                context.get(token),
                () -> this.fromProviderRegistry(globalExecutionContextProviderRegistry, token, context)
        );
    }

    @Override
    public <T> Optional<T> get(Token<T> token, ExecutionContext context, Object bean) {
        return OptionalUtils.firstPresent(
                context.get(token),
                () -> this.fromProviderRegistry(beanManager.getProviderRegistry(bean), token, context),
                () -> this.fromProviderRegistry(globalExecutionContextProviderRegistry, token, context)
        );
    }

    private <T> Optional<T> fromProviderRegistry(ExecutionContextProviderRegistry executionContextProviderRegistry, Token<T> token, ExecutionContext context) {
        return executionContextProviderRegistry.findFor(token).map(provider -> provider.get(token, context));
    }

    @Override
    public Object invokeMethod(CachedMethod method, Object instance, ExecutionContext context) {
        return ReflectionUtils.invoke(method.getMethod(), instance, resolveParameters(method, context));
    }

    private Object[] resolveParameters(CachedMethod method, ExecutionContext context) {
        Object[] parameters = new Object[method.getParameters().size()];

        int i = 0;
        for (CachedParameter parameter : method.getParameters()) {
            parameters[i++] = this.get(ParameterToken.build(method, parameter), context).orElse(null);
        }

        return parameters;
    }
}
