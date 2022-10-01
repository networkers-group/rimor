package st.networkers.rimor.internal.inject;

import lombok.Getter;

import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.Map;

@Getter
public class Token {

    private final Class<?> type;
    private final Map<Class<? extends Annotation>, Annotation> annotations;

    public Token(Class<?> type) {
        this(type, new HashMap<>());
    }

    public Token(Class<?> type, Map<Class<? extends Annotation>, Annotation> annotations) {
        this.type = type;
        this.annotations = annotations;
    }

    public Token withAnnotation(Annotation annotation) {
        this.annotations.put(annotation.annotationType(), annotation);
        return this;
    }

    @SuppressWarnings("unchecked")
    public <T extends Annotation> T getAnnotation(Class<T> annotationClass) {
        return (T) this.annotations.get(annotationClass);
    }
}
