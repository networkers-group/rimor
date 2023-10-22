package st.networkers.rimor.qualify;

import st.networkers.rimor.qualify.reflect.QualifiedElement;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;

public class ImmutableToken extends Token {

    public static ImmutableToken copyOf(Token token) {
        return new ImmutableToken(token.getType(),
                Collections.unmodifiableMap(token.getQualifiersMap()),
                Collections.unmodifiableCollection(token.getRequiredQualifiers()));
    }

    protected ImmutableToken(Type type,
                           Map<Class<? extends Annotation>, Annotation> qualifiers,
                           Collection<Class<? extends Annotation>> requiredQualifiers) {
        super(type, qualifiers, requiredQualifiers);
    }

    @Override
    public Token qualifiedWith(Annotation qualifierAnnotation) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void addQualifier(Annotation qualifierAnnotation) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Token qualifiedWith(Class<? extends Annotation> qualifierType) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void addRequiredQualifier(Class<? extends Annotation> qualifierType) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Token withQualifiers(Map<Class<? extends Annotation>, Annotation> qualifiers) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void addQualifiers(Map<Class<? extends Annotation>, Annotation> qualifiers) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Token withRequiredQualifiers(Collection<Class<? extends Annotation>> qualifierTypes) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void addRequiredQualifiers(Collection<Class<? extends Annotation>> qualifierTypes) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Token withQualifiersOf(QualifiedElement element) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void addQualifiersOf(QualifiedElement element) {
        throw new UnsupportedOperationException();
    }
}
