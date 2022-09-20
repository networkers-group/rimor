package st.networkers.rimor.internal.reflect;

import lombok.Getter;
import st.networkers.rimor.util.ReflectionUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Parameter;
import java.util.Map;

@Getter
public class CachedParameter extends CachedAnnotatedElement {

    public static CachedParameter build(Parameter parameter) {
        return new CachedParameter(parameter.getType(), ReflectionUtils.getMappedAnnotations(parameter));
    }

    private final Class<?> type;

    public CachedParameter(Class<?> type, Map<Class<? extends Annotation>, Annotation> annotations) {
        super(annotations);
        this.type = type;
    }
}
