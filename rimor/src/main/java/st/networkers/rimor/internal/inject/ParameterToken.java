package st.networkers.rimor.internal.inject;

import lombok.Getter;
import st.networkers.rimor.inject.Token;
import st.networkers.rimor.reflect.CachedMethod;
import st.networkers.rimor.reflect.CachedParameter;

@Getter
public class ParameterToken<T> extends Token<T> {

    public static ParameterToken<?> build(CachedMethod method, CachedParameter parameter) {
        return new ParameterToken<>(method, parameter, parameter.getType());
    }

    private final CachedMethod method;
    private final CachedParameter parameter;

    public ParameterToken(CachedMethod method, CachedParameter parameter, Class<T> type) {
        super(type, parameter.getAnnotationsMap());
        this.method = method;
        this.parameter = parameter;
    }
}
