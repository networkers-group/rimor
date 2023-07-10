package st.networkers.rimor.params.parse.support;

import java.lang.annotation.Annotation;
import java.util.Arrays;

@SuppressWarnings("ClassExplicitlyAnnotation")
public class TrueValuesImpl implements TrueValues {

    private final String[] value;

    public TrueValuesImpl(String... value) {
        this.value = value;
    }

    @Override
    public String[] value() {
        return value;
    }

    @Override
    public Class<? extends Annotation> annotationType() {
        return TrueValues.class;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TrueValuesImpl)) return false;
        TrueValuesImpl that = (TrueValuesImpl) o;
        return Arrays.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        return 127 * "value".hashCode() ^ Arrays.hashCode(value);
    }
}
