package st.networkers.rimor.reflect;

import st.networkers.rimor.util.ReflectionUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Parameter;
import java.lang.reflect.Type;
import java.util.Map;

public class CachedParameter extends CachedAnnotatedElement {

    public static CachedParameter build(Parameter parameter) {
        return new CachedParameter(parameter, ReflectionUtils.getMappedAnnotations(parameter), parameter.getParameterizedType());
    }

    private final Parameter parameter;
    private final Type type;

    public CachedParameter(Parameter parameter, Map<Class<? extends Annotation>, Annotation> annotations, Type type) {
        super(annotations);
        this.parameter = parameter;
        this.type = type;
    }

    public Parameter getParameter() {
        return parameter;
    }

    public Type getType() {
        return type;
    }
}
