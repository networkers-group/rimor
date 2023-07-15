package st.networkers.rimor.bean;

import st.networkers.rimor.context.provide.ExecutionContextProviderRegistry;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class BeanManager {

    private final Collection<BeanProcessor> beanProcessors = new ArrayList<>();

    private final Map<Object, ExecutionContextProviderRegistry> providerRegistries = new HashMap<>();

    public void registerBeanProcessor(BeanProcessor beanProcessor) {
        this.beanProcessors.add(beanProcessor);
    }

    public void processBean(Object bean) {
        this.beanProcessors.forEach(beanProcessor -> beanProcessor.process(bean));
    }

    public ExecutionContextProviderRegistry getProviderRegistry(Object bean) {
        return this.providerRegistries.get(bean);
    }
}
