package st.networkers.rimor.internal.inject;

import com.google.common.reflect.TypeToken;
import lombok.Getter;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.Map;

@Getter
public class Token<T> extends Annotated<Token<T>> {

    private final TypeToken<T> type;

    public Token(Class<T> type) {
        this(TypeToken.of(type));
    }

    public Token(TypeToken<T> type) {
        this.type = type;
    }

    public Token(Class<T> type, Map<Class<? extends Annotation>, Annotation> annotations) {
        super(annotations, new ArrayList<>());
        this.type = TypeToken.of(type);
    }
}