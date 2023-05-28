package st.networkers.rimor.internal.inject;

import com.google.common.reflect.TypeToken;
import st.networkers.rimor.inject.AnnotatedProperties;
import st.networkers.rimor.inject.Token;
import st.networkers.rimor.reflect.CachedMethod;
import st.networkers.rimor.reflect.CachedParameter;

import java.util.Objects;

public class ParameterToken<T> extends Token<T> {

    public static ParameterToken<?> build(CachedMethod method, CachedParameter parameter) {
        return new ParameterToken<>(method, parameter, TypeToken.of(parameter.getType()));
    }

    private final CachedMethod method;
    private final CachedParameter parameter;

    private ParameterToken(CachedMethod method, CachedParameter parameter, TypeToken<T> type) {
        super(type, AnnotatedProperties.build(parameter));
        this.method = method;
        this.parameter = parameter;
    }

    public CachedMethod getMethod() {
        return method;
    }

    public CachedParameter getParameter() {
        return parameter;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ParameterToken)) return false;
        if (!super.equals(o)) return false;
        ParameterToken<?> that = (ParameterToken<?>) o;
        return Objects.equals(method, that.method) && Objects.equals(parameter, that.parameter);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), method, parameter);
    }
}
