package st.networkers.rimor.params;

import java.lang.annotation.Annotation;
import java.util.Objects;

@SuppressWarnings("ClassExplicitlyAnnotation")
public class InstructionParamImpl implements InstructionParam {

    private final String value;
    private final String description;
    private final int index;

    public InstructionParamImpl(String value, String description, int index) {
        this.value = value;
        this.description = description;
        this.index = index;
    }

    @Override
    public String value() {
        return value;
    }

    @Override
    public String description() {
        return description;
    }

    @Override
    public int index() {
        return index;
    }

    @Override
    public Class<? extends Annotation> annotationType() {
        return InstructionParam.class;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof InstructionParamImpl)) return false;
        InstructionParamImpl param = (InstructionParamImpl) o;
        return index == param.index && Objects.equals(value, param.value) && Objects.equals(description, param.description);
    }

    @Override
    public int hashCode() {
        return 127 * "value".hashCode() ^ value.hashCode() + 127 * "description".hashCode() ^ description.hashCode() + 127 * "index".hashCode() ^ Integer.hashCode(index);
    }
}
