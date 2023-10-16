package st.networkers.rimor.bean;

/**
 * Signals that something went wrong while processing a bean.
 */
public class BeanProcessingException extends RuntimeException {

    private final Object bean;
    
    public BeanProcessingException(Object bean) {
        this(bean, "there was an error while processing " + bean.getClass().getName());
    }

    public BeanProcessingException(Object bean, String message) {
        super(message);
        this.bean = bean;
    }

    public BeanProcessingException(Object bean, String message, Throwable cause) {
        super(message, cause);
        this.bean = bean;
    }

    public BeanProcessingException(Object bean, Throwable cause) {
        this(bean, "there was an error while processing " + bean.getClass().getName(), cause);
    }

    public Object getBean() {
        return bean;
    }
}
