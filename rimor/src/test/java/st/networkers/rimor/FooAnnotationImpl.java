package st.networkers.rimor;

import java.lang.annotation.Annotation;
import java.util.Objects;

@SuppressWarnings("ClassExplicitlyAnnotation")
public class FooAnnotationImpl implements FooAnnotation {
    private final String value;

    public FooAnnotationImpl(String value) {
        this.value = value;
    }

    @Override
    public String value() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof FooAnnotation)) return false;
        FooAnnotation fooAnnotation = (FooAnnotation) o;
        return Objects.equals(value, fooAnnotation.value());
    }

    @Override
    public Class<? extends Annotation> annotationType() {
        return FooAnnotation.class;
    }
}
