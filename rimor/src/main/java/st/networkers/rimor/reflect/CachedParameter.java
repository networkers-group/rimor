package st.networkers.rimor.reflect;

import lombok.Getter;
import st.networkers.rimor.util.ReflectionUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Parameter;
import java.util.Map;

@Getter
public class CachedParameter extends CachedAnnotatedElement {

    public static CachedParameter build(Parameter parameter) {
        return new CachedParameter(parameter, ReflectionUtils.getMappedAnnotations(parameter), parameter.getType());
    }

    private final Parameter parameter;
    private final Class<?> type;

    public CachedParameter(Parameter parameter, Map<Class<? extends Annotation>, Annotation> annotations, Class<?> type) {
        super(annotations);
        this.parameter = parameter;
        this.type = type;
    }
}
