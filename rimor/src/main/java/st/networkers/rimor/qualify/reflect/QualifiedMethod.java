package st.networkers.rimor.qualify.reflect;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Collectors;

public class QualifiedMethod extends QualifiedElement {

    public static QualifiedMethod build(Method method) {
        return new QualifiedMethod(
                method,
                QualifiedElement.getMappedQualifiers(method),
                QualifiedElement.getRequiredQualifiers(method),
                Arrays.stream(method.getParameters())
                        .map(QualifiedParameter::build)
                        .collect(Collectors.toList())
        );
    }

    private final Method method;
    private final List<QualifiedParameter> qualifiedParameters;

    public QualifiedMethod(Method method,
                           Map<Class<? extends Annotation>, Annotation> qualifiers,
                           Collection<Class<? extends Annotation>> requiredQualifiers,
                           List<QualifiedParameter> qualifiedParameters) {
        super(qualifiers, requiredQualifiers);
        this.method = method;
        this.qualifiedParameters = qualifiedParameters;
    }

    public List<QualifiedParameter> getQualifiedParameters() {
        return Collections.unmodifiableList(this.qualifiedParameters);
    }

    public Method getMethod() {
        return method;
    }
}
