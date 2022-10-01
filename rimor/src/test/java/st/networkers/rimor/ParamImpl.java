package st.networkers.rimor;

import st.networkers.rimor.provide.builtin.Param;

import java.lang.annotation.Annotation;

public class ParamImpl implements Param {
    private final int value;

    public ParamImpl(int value) {
        this.value = value;
    }

    @Override
    public int value() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Param)) return false;
        Param param = (Param) o;
        return value == param.value();
    }

    @Override
    public Class<? extends Annotation> annotationType() {
        return Param.class;
    }
}
