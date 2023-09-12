package st.networkers.rimor.reflect;

import st.networkers.rimor.util.ReflectionUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Collectors;

public class CachedMethod extends CachedQualifiedElement {

    public static CachedMethod build(Method method) {
        return new CachedMethod(method,
                ReflectionUtils.getMappedQualifiers(method),
                ReflectionUtils.getRequiredQualifiers(method),
                Arrays.stream(method.getParameters())
                        .map(CachedParameter::build)
                        .collect(Collectors.toList())
        );
    }

    private final Method method;
    private final List<CachedParameter> parameters;

    public CachedMethod(Method method,
                        Map<Class<? extends Annotation>, Annotation> annotations,
                        Collection<Class<? extends Annotation>> requiredAnnotations,
                        List<CachedParameter> parameters) {
        super(annotations, requiredAnnotations);
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
