package com.bobocode.petros.bring.utils;

import com.bobocode.petros.bring.annotation.Bean;
import com.bobocode.petros.bring.annotation.Component;
import com.bobocode.petros.bring.annotation.Repository;
import com.bobocode.petros.bring.annotation.Service;
import com.bobocode.petros.bring.annotation.Configuration;

import lombok.experimental.UtilityClass;

import java.lang.reflect.Method;
import java.util.Objects;

/**
 * Utility class to resolve bean name from class or method
 */
@UtilityClass
public final class BeanNameUtils {

    /**
     * Resolve bean name from class.
     * 1. If class annotated by {@link Component}, {@link Service}, or {@link Repository} annotation and
     * {@link Component#name()} method returns not empty value, then return name from annotation.
     * 2. If the name from annotation is blank then return the class name in the camel case.
     *
     * @param clazz bean class
     * @return bean name
     */
    public static String getBeanName(Class<?> clazz) {
        Objects.requireNonNull(clazz, "Input bean class must not be null!");
        var className = clazz.getSimpleName();
        if (clazz.isAnnotationPresent(Component.class)) {
            return createClassBeanName(className, clazz.getAnnotation(Component.class).name());
        }
        if (clazz.isAnnotationPresent(Service.class)) {
            return createClassBeanName(className, clazz.getAnnotation(Service.class).name());
        }
        if (clazz.isAnnotationPresent(Repository.class)) {
            return createClassBeanName(className, clazz.getAnnotation(Repository.class).name());
        }
        throw new IllegalArgumentException("Class %s isn't annotated by @Component, @Service or @Repository annotation".formatted(clazz.getName()));
    }

    private static String createClassBeanName(String className, String nameFromAnnotation) {
        return nameFromAnnotation.isEmpty() ?
                className.substring(0, 1).toLowerCase() + className.substring(1) :
                nameFromAnnotation;
    }

    /**
     * Helper method hat allows to create a bean name from a given class name.
     *
     * @param className class name as a string
     * @return bean name
     */
    public static String createBeanName(final String className) {
        return createClassBeanName(className, "");
    }

    /**
     * Resolve bean name from method inside class annotated by {@link Configuration}.
     * 1. If method annotated by {@link Bean} annotation and {@link Bean#name()} method returns not empty value,
     * then return name from annotation.
     * 2. If the name from annotation is blank then return the method name.
     *
     * @param method configuration bean method
     * @return bean name
     */
    public static String getBeanName(Method method) {
        var annotation = method.getAnnotation(Bean.class);
        return annotation.name().isEmpty() ? method.getName() : annotation.name();
    }
}
