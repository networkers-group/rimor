package st.networkers.rimor.util;

import com.google.common.reflect.TypeToken;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Map;
import java.util.Optional;

public final class ReflectionUtils {

    private ReflectionUtils() {
    }

    public static Map<Class<? extends Annotation>, Annotation> getMappedAnnotations(AnnotatedElement element) {
        return InspectionUtils.getMappedAnnotations(Arrays.asList(element.getAnnotations()));
    }

    public static TypeToken<?> unwrapOptional(TypeToken<Optional<?>> optionalTypeToken) {
        return optionalTypeToken.resolveType(Optional.class.getTypeParameters()[0]);
    }

    public static Object invoke(Method method, Object instance, Object[] parameters) {
        try {
            return method.invoke(instance, parameters);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e.getTargetException());
        }
    }
}
