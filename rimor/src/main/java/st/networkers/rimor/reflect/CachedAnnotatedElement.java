package st.networkers.rimor.reflect;

import st.networkers.rimor.annotation.Annotated;

import java.lang.annotation.Annotation;
import java.util.*;

public class CachedAnnotatedElement implements Annotated {

    private final Map<Class<? extends Annotation>, Annotation> annotations;
    private final Collection<Class<? extends Annotation>> requiredAnnotations;

    public CachedAnnotatedElement(Map<Class<? extends Annotation>, Annotation> annotations,
                                  Collection<Class<? extends Annotation>> requiredAnnotations) {
        this.annotations = annotations;
        this.requiredAnnotations = requiredAnnotations;
    }

    @Override
    public Collection<Class<? extends Annotation>> getRequiredAnnotations() {
        return Collections.unmodifiableCollection(this.requiredAnnotations);
    }

    @Override
    public Map<Class<? extends Annotation>, Annotation> getAnnotationsMap() {
        return Collections.unmodifiableMap(this.annotations);
    }
}
