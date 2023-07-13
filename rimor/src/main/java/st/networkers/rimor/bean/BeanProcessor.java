package st.networkers.rimor.bean;

/**
 * Allows processing beans after they are registered into Rimor.
 */
public interface BeanProcessor {

    void process(Object bean);
}
