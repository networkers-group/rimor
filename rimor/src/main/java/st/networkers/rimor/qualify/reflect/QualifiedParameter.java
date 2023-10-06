package st.networkers.rimor.qualify.reflect;

import java.lang.annotation.Annotation;
import java.lang.reflect.Parameter;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.Map;

public class QualifiedParameter extends QualifiedElement {

    public static QualifiedParameter build(Parameter parameter) {
        return new QualifiedParameter(
                parameter,
                parameter.getParameterizedType(),
                QualifiedElement.getMappedQualifiers(parameter),
                QualifiedElement.getRequiredQualifiers(parameter)
        );
    }

    private final Parameter parameter;
    private final Type type;

    public QualifiedParameter(Parameter parameter,
                              Type type,
                              Map<Class<? extends Annotation>, Annotation> qualifiers,
                              Collection<Class<? extends Annotation>> requiredQualifiers) {
        super(qualifiers, requiredQualifiers);
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
