package com.bobocode.petros.bring.utils;

import com.bobocode.petros.bring.annotation.Bean;
import com.bobocode.petros.bring.annotation.Component;
import com.bobocode.petros.bring.annotation.Repository;
import com.bobocode.petros.bring.annotation.Service;
import lombok.experimental.UtilityClass;

import java.lang.reflect.Method;

@UtilityClass
public class BeanNameUtils {

    public String getBeanName(Class<?> clazz) {
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

    private String createClassBeanName(String className, String nameFromAnnotation) {
        return nameFromAnnotation.isEmpty() ?
                className.substring(0, 1).toLowerCase() + className.substring(1) :
                nameFromAnnotation;
    }

    public String getBeanName(Method method) {
        var annotation = method.getAnnotation(Bean.class);
        return annotation.name().isEmpty() ? method.getName() : annotation.name();
    }
}
