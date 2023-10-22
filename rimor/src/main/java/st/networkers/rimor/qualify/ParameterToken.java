package st.networkers.rimor.qualify;

import st.networkers.rimor.qualify.reflect.QualifiedMethod;
import st.networkers.rimor.qualify.reflect.QualifiedParameter;

import java.util.Collections;
import java.util.Objects;

/**
 * A token that represents a method parameter.
 */
public class ParameterToken extends ImmutableToken {

    public static ParameterToken build(QualifiedMethod method, QualifiedParameter parameter) {
        return new ParameterToken(method, parameter);
    }

    private final QualifiedMethod method;
    private final QualifiedParameter parameter;

    private ParameterToken(QualifiedMethod method, QualifiedParameter parameter) {
        super(parameter.getType(),
                Collections.unmodifiableMap(parameter.getQualifiersMap()),
                Collections.unmodifiableCollection(parameter.getRequiredQualifiers()));
        this.method = method;
        this.parameter = parameter;
    }

    public QualifiedMethod getQualifiedMethod() {
        return method;
    }

    public QualifiedParameter getQualifiedParameter() {
        return parameter;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ParameterToken)) return false;
        if (!super.equals(o)) return false;
        ParameterToken that = (ParameterToken) o;
        return Objects.equals(method, that.method) && Objects.equals(parameter, that.parameter);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), method, parameter);
    }

    @Override
    public String toString() {
        return "ParameterToken{" +
               "method=" + method +
               ", parameter=" + parameter +
               ", type=" + type +
               ", qualifiers=" + qualifiers +
               ", requiredQualifiers=" + requiredQualifiers +
               '}';
    }
}
