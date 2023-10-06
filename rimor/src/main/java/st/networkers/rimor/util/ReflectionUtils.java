package st.networkers.rimor.util;

import org.apache.commons.lang3.ClassUtils;
import org.apache.commons.lang3.reflect.TypeUtils;

import java.lang.reflect.*;
import java.util.Optional;

public final class ReflectionUtils {

    private ReflectionUtils() {
    }

    public static Type wrapPrimitive(Type primitiveType) {
        return primitiveType instanceof Class<?> ? ClassUtils.primitiveToWrapper((Class<?>) primitiveType) : primitiveType;
    }

    @SuppressWarnings("rawtypes")
    public static Type unwrapOptional(Type optionalType) {
        TypeVariable<Class<Optional>> T = Optional.class.getTypeParameters()[0];

        return TypeUtils.getTypeArguments(optionalType, Optional.class).get(T);
    }

    public static Object instantiateInnerClass(Object enclosingInstance, Class<?> innerClass) {
        try {
            return Modifier.isStatic(innerClass.getModifiers())
                    ? innerClass.getConstructor().newInstance()
                    : innerClass.getConstructor(enclosingInstance.getClass()).newInstance(enclosingInstance);
        } catch (NoSuchMethodException e) {
            throw new IllegalArgumentException("Cannot instantiate " + innerClass.getName() + " class because it " +
                                               "does not declare a public no-args constructor", e);
        } catch (InvocationTargetException | InstantiationException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
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
