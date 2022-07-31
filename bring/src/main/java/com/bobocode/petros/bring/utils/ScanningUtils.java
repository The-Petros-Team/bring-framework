package com.bobocode.petros.bring.utils;

import lombok.experimental.UtilityClass;
import org.reflections.Reflections;

import java.util.Set;

@UtilityClass
public class ScanningUtils {

    private static final String BEAN_POST_PROCESSORS_PACKAGE = "com.bobocode.petros.bring.factory.postprocessor";

    public Set<Class<?>> getClassesFromPackage(final String packageName) {
        if (packageName == null || packageName.isBlank()) {
            throw new IllegalArgumentException(String.format("Invalid package '%s'", packageName));
        }

        Reflections reflections = new Reflections(packageName, BEAN_POST_PROCESSORS_PACKAGE);
        return reflections.getSubTypesOf(Object.class);
    }
}
