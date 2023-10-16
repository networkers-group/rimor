package st.networkers.rimor.qualify.reflect;

import java.lang.annotation.Annotation;
import java.util.Collection;
import java.util.Map;
import java.util.Objects;

public class QualifiedClass extends QualifiedElement {

    public static QualifiedClass build(Class<?> clazz) {
        return new QualifiedClass(
                clazz,
                QualifiedElement.getMappedQualifiers(clazz),
                QualifiedElement.getRequiredQualifiers(clazz)
        );
    }

    private final Class<?> clazz;

    public QualifiedClass(Class<?> clazz,
                          Map<Class<? extends Annotation>, Annotation> qualifiers,
                          Collection<Class<? extends Annotation>> requiredQualifiers) {
        super(qualifiers, requiredQualifiers);
        this.clazz = clazz;
    }

    public Class<?> getClazz() {
        return clazz;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof QualifiedClass)) return false;
        if (!super.equals(o)) return false;
        QualifiedClass that = (QualifiedClass) o;
        return Objects.equals(clazz, that.clazz);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), clazz);
    }
}
