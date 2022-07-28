package com.bobocode.petros.bring.utils;

import lombok.experimental.UtilityClass;
import org.reflections.Reflections;
import org.reflections.scanners.Scanners;
import org.reflections.util.ConfigurationBuilder;

import java.util.Set;

@UtilityClass
public class ScanningUtils {

    private static final String BEAN_POST_PROCESSORS_PACKAGE = "com.bobocode.petros.bring.factory.postprocessor";

    public Set<Class<?>> getClassesFromPackage(final String packageName) {
        if (packageName == null || packageName.isBlank()) {
            throw new IllegalArgumentException(String.format("Invalid package '%s'", packageName));
        }
        var configBuilder = new ConfigurationBuilder()
                .addScanners(Scanners.SubTypes.filterResultsBy(s -> true))
                .forPackages(packageName, BEAN_POST_PROCESSORS_PACKAGE);
        var reflections = new Reflections(configBuilder);
        return reflections.getSubTypesOf(Object.class);
    }
}
