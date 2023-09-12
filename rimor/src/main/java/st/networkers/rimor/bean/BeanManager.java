package st.networkers.rimor.bean;

import java.util.ArrayList;
import java.util.Collection;

public class BeanManager {

    private final Collection<BeanProcessor> beanProcessors = new ArrayList<>();

    public void registerBeanProcessor(BeanProcessor beanProcessor) {
        this.beanProcessors.add(beanProcessor);
    }

    public void processBean(Object bean) {
        this.beanProcessors.forEach(beanProcessor -> beanProcessor.process(bean));
    }
}
