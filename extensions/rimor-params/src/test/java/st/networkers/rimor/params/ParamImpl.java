package st.networkers.rimor.params;

import java.lang.annotation.Annotation;

@SuppressWarnings("ClassExplicitlyAnnotation")
public class ParamImpl implements Param {

    private final int position;

    public ParamImpl(int position) {
        this.position = position;
    }

    @Override
    public String value() {
        return null;
    }

    @Override
    public String description() {
        return null;
    }

    @Override
    public int position() {
        return position;
    }

    @Override
    public Class<? extends Annotation> annotationType() {
        return Param.class;
    }
}
