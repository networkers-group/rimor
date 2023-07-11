package st.networkers.rimor.reflect;

import st.networkers.rimor.util.ReflectionUtils;

import java.lang.annotation.Annotation;
import java.util.Collection;
import java.util.Map;

public class CachedClass extends CachedAnnotatedElement {

    public static CachedClass build(Class<?> clazz) {
        return new CachedClass(clazz,
                ReflectionUtils.getMappedAnnotations(clazz),
                ReflectionUtils.getRequiredAnnotations(clazz)
        );
    }

    private final Class<?> clazz;

    public CachedClass(Class<?> clazz,
                       Map<Class<? extends Annotation>, Annotation> annotations,
                       Collection<Class<? extends Annotation>> requiredAnnotations) {
        super(annotations, requiredAnnotations);
        this.clazz = clazz;
    }

    public Class<?> getClazz() {
        return clazz;
    }
}
