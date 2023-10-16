package st.networkers.rimor.qualify.reflect;

import java.lang.annotation.Annotation;
import java.lang.reflect.Parameter;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.Map;
import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof QualifiedParameter)) return false;
        if (!super.equals(o)) return false;
        QualifiedParameter that = (QualifiedParameter) o;
        return Objects.equals(parameter, that.parameter) && Objects.equals(type, that.type);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), parameter, type);
    }
}
