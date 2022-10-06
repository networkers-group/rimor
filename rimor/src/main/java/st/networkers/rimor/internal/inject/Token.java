package st.networkers.rimor.internal.inject;

import com.google.common.reflect.TypeToken;
import lombok.Getter;

import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.Map;

@Getter
public class Token<T> {

    private final TypeToken<T> type;
    private final Map<Class<? extends Annotation>, Annotation> annotations;

    public Token(Class<T> type) {
        this(TypeToken.of(type));
    }

    public Token(TypeToken<T> type) {
        this(type, new HashMap<>());
    }

    public Token(TypeToken<T> type, Map<Class<? extends Annotation>, Annotation> annotations) {
        this.type = type;
        this.annotations = annotations;
    }

    public Token<T> withAnnotation(Annotation annotation) {
        this.annotations.put(annotation.annotationType(), annotation);
        return this;
    }

    @SuppressWarnings("unchecked")
    public <A extends Annotation> A getAnnotation(Class<A> annotationClass) {
        return (A) this.annotations.get(annotationClass);
    }
}
