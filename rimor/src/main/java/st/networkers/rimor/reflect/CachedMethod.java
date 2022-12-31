package st.networkers.rimor.reflect;

import st.networkers.rimor.util.ReflectionUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class CachedMethod extends CachedAnnotatedElement {

    public static CachedMethod build(Method method) {
        return new CachedMethod(
                method,
                ReflectionUtils.getMappedAnnotations(method),
                Arrays.stream(method.getParameters())
                        .map(CachedParameter::build)
                        .collect(Collectors.toList())
        );
    }

    private final Method method;
    private final List<CachedParameter> parameters;

    public CachedMethod(Method method, Map<Class<? extends Annotation>, Annotation> annotations, List<CachedParameter> parameters) {
        super(annotations);
        this.method = method;
        this.parameters = parameters;
    }

    public List<CachedParameter> getParameters() {
        return Collections.unmodifiableList(this.parameters);
    }

    public Method getMethod() {
        return method;
    }
}
