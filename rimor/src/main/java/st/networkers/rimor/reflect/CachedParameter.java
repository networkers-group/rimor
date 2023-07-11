package st.networkers.rimor.reflect;

import st.networkers.rimor.util.ReflectionUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Parameter;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.Map;

public class CachedParameter extends CachedAnnotatedElement {

    public static CachedParameter build(Parameter parameter) {
        return new CachedParameter(
                parameter,
                parameter.getParameterizedType(),
                ReflectionUtils.getMappedAnnotations(parameter),
                ReflectionUtils.getRequiredAnnotations(parameter)
        );
    }

    private final Parameter parameter;
    private final Type type;

    public CachedParameter(Parameter parameter,
                           Type type,
                           Map<Class<? extends Annotation>, Annotation> annotations,
                           Collection<Class<? extends Annotation>> requiredAnnotations) {
        super(annotations, requiredAnnotations);
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
