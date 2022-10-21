package st.networkers.rimor;

import java.lang.annotation.Annotation;

@SuppressWarnings("ClassExplicitlyAnnotation")
public class BarAnnotationImpl implements BarAnnotation {
    private final int value;

    public BarAnnotationImpl(int value) {
        this.value = value;
    }

    @Override
    public int value() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof BarAnnotation)) return false;
        BarAnnotation barAnnotation = (BarAnnotation) o;
        return value == barAnnotation.value();
    }

    @Override
    public Class<? extends Annotation> annotationType() {
        return BarAnnotation.class;
    }
}
