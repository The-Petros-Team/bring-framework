package com.bobocode.petros.bring.utils;

import com.bobocode.petros.bring.annotation.Component;
import com.bobocode.petros.bring.annotation.Configuration;
import com.bobocode.petros.bring.annotation.Repository;
import com.bobocode.petros.bring.annotation.Service;
import com.bobocode.petros.bring.context.domain.BeanDefinition;
import com.bobocode.petros.bring.exception.IllegalBeanDefinitionStateException;
import lombok.experimental.UtilityClass;
import org.reflections.Reflections;
import org.reflections.scanners.Scanners;
import org.reflections.util.ConfigurationBuilder;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static com.bobocode.petros.bring.exception.ExceptionMessage.CLASS_IS_NOT_REGISTERED_AS_BEAN_CANDIDATE;
import static com.bobocode.petros.bring.exception.ExceptionMessage.INTERFACE_HAS_MORE_THEN_ONE_IMPLEMENTATION;

@UtilityClass
public class ScanningUtils {

    /**
     * Collects classes from the given packages.
     *
     * @param packages packages to scan
     * @return set of classes from the scanned packages
     */
    public Set<Class<?>> getClassesFromPackages(final String... packages) {
        if (packages == null || packages.length == 0) {
            throw new IllegalArgumentException("Null or empty packages! Please provide package names that are not null or empty!");
        }
        var configBuilder = new ConfigurationBuilder()
                .addScanners(Scanners.SubTypes.filterResultsBy(s -> true))
                .forPackages(packages);
        return new Reflections(configBuilder).getSubTypesOf(Object.class);
    }

    /**
     * Returns implementations for a given interface.
     *
     * @param classes     classes to scan
     * @param anInterface an interface which implementations should be found
     * @return list of implementations
     */
    public static List<Class<?>> findImplementations(final Set<Class<?>> classes, final Class<?> anInterface) {
        return classes.stream()
                .filter(clazz -> !clazz.isInterface())
                .filter(anInterface::isAssignableFrom)
                .toList();
    }

    /**
     * Checks whether an interface has no more that a single implementation and throws an exception if yes.
     *
     * @param implementations number of implementations
     * @param type            interface name
     */
    public static void checkImplementations(final int implementations, final String type) {
        if (implementations != 1) {
            throw new IllegalBeanDefinitionStateException(String.format(INTERFACE_HAS_MORE_THEN_ONE_IMPLEMENTATION, type));
        }
    }

    /**
     * Checks whether a class is annotated with one of the following annotations: @Component, @Service, @Repository.
     *
     * @param type class that is under a check
     * @return true if the class is annotated via one of the given annotations or false otherwise
     */
    public static boolean isRegisteredAsComponent(final Class<?> type) {
        return type.isAnnotationPresent(Component.class)
                || type.isAnnotationPresent(Service.class)
                || type.isAnnotationPresent(Repository.class);
    }

    /**
     * Checks whether a class is registered as a bean candidate. Throws an exception if condition mismatched.
     *
     * @param implementation              class that is not registered properly
     * @param isRegisteredAsBeanCandidate condition to check
     */
    public static void checkBeanCandidate(final Class<?> implementation, final boolean isRegisteredAsBeanCandidate) {
        if (!isRegisteredAsBeanCandidate) {
            throw new IllegalArgumentException(String.format(CLASS_IS_NOT_REGISTERED_AS_BEAN_CANDIDATE, implementation.getName()));
        }
    }

    /**
     * Filters out configuration classes from a set of all classes from a root package.
     *
     * @param classes classes to filter
     * @return set of configuration classes
     */
    public static Set<Class<?>> getConfigurationClasses(final Set<Class<?>> classes) {
        return classes.stream()
                .filter(clazz -> clazz.isAnnotationPresent(Configuration.class))
                .collect(Collectors.toSet());
    }

    /**
     * Checks whether a class is registered in any of the given configuration classes.
     *
     * @param classes classes to filter
     * @param type    class that is supposed to be found among the given set of classes
     * @return true if class is registered in one of a given configuration classes or false otherwise
     */
    public static boolean isRegisteredGlobally(final Set<Class<?>> classes, final Class<?> type) {
        return classes.stream()
                .flatMap(clazz -> Arrays.stream(clazz.getDeclaredMethods()))
                .anyMatch(method -> method.getReturnType().isAssignableFrom(type));
    }

    /**
     * During bean creation phase .
     *
     * @param classes           classes to filter
     * @param name              unique key for storing in collection
     * @param beanDefinition    bean definition object for post configuring an interface related fields
     * @param beanCandidate     class candidate for creation bean definition
     */
    public void handleInterfaceDuringBeanDefinitionCreation(Set<Class<?>> classes,
                                                            String name,
                                                            BeanDefinition beanDefinition,
                                                            Class<?> beanCandidate) {
        beanDefinition.setInterface(true);
        final List<Class<?>> implementations = findImplementations(classes, beanCandidate);
        final String typeName = beanCandidate.getName();
        final int size = implementations.size();
        checkImplementations(size, typeName);
        final Map<String, Object> beanDefinitionImplementations = beanDefinition.getImplementations();
        implementations.forEach(impl -> beanDefinitionImplementations.put(name, impl));
    }
}
