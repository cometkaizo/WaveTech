package me.cometkaizo.util;

import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.type.filter.AssignableTypeFilter;
import org.springframework.core.type.filter.TypeFilter;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@SuppressWarnings("unused")
public abstract class ClassUtils {

    private static final Set<Class<?>> primitiveWrappers = Set.of(
            Boolean.class,
            Character.class,
            Byte.class,
            Short.class,
            Integer.class,
            Long.class,
            Float.class,
            Double.class,
            Void.class
    );

    public static Set<Class<?>> getClassesByFilter(TypeFilter filter) {

        ClassPathScanningCandidateComponentProvider provider = new ClassPathScanningCandidateComponentProvider(false);
        provider.addIncludeFilter(filter);

        Set<BeanDefinition> definitions = provider.findCandidateComponents("me.cometkaizo");

        return definitions.stream().map(bean -> forNameStrict(bean.getBeanClassName())).collect(Collectors.toUnmodifiableSet());

    }

    public static <T> Set<Class<? extends T>> getSubclasses(Class<? extends T> clazz) {

        Set<Class<?>> classes = getClassesByFilter(new AssignableTypeFilter(clazz));

        Set<Class<? extends T>> castedClasses = new HashSet<>(classes.size());

        for (Class<?> cl : classes) {
            castedClasses.add(cl.asSubclass(clazz));
        }

        return castedClasses;

    }

    public static <T> Class<? super T> getSuperclassBefore(Class<? super T> clazz, Class<? super T> limit) {
        if (clazz.getSuperclass().equals(limit)) return clazz;
        return getSuperclassBefore(clazz.getSuperclass(), limit);
    }

    public static boolean isPrimitiveWrapper(Class<?> clazz) {
        return primitiveWrappers.contains(clazz);
    }

    public static Class<?> getGenericType(Object obj) {
        Class<?> genericType = null;
        Type gnrcType = obj.getClass().getGenericSuperclass();
        if (gnrcType instanceof ParameterizedType parameterizedType) {
            Type[] types = parameterizedType.getActualTypeArguments();

            if (types != null && types.length > 0) {
                Type type = types[0];
                if (type instanceof Class) {
                    genericType = (Class<?>) type;
                }
            }
        }
        return genericType;
    }

    /**
     * Gets the class with the specified name. <br>
     * Should be used when you are certain that a class exists with a certain name, and so is
     * unnecessary to deal with the exception. <br>
     * If you are <i>not</i> certain that there is a class corresponding to the given name,
     * use {@link Class#forName(String)} instead.
     * @throws RuntimeException if there is no class with the given name
     * @param name the name of the class
     * @return the class with the specified name
     */
    public static @NotNull Class<?> forNameStrict(String name) {
        try {
            return Class.forName(name);
        } catch (ClassNotFoundException c) {
            throw new RuntimeException(c);
        }
    }

    public static @NotNull Class<?> forNameStrict(Module module, String name) {
        Class<?> result = Class.forName(module, name);
        if (result != null)
            return result;
        throw new RuntimeException(new ClassNotFoundException(name));
    }

    public static @NotNull Class<?> forNameStrict(String name, boolean initialize, ClassLoader loader) {
        try {
            return Class.forName(name, initialize, loader);
        } catch (ClassNotFoundException c) {
            throw new RuntimeException(c);
        }
    }

}
