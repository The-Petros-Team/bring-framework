package com.bobocode.petros.bring.scanner.impl;

import com.bobocode.petros.bring.annotation.*;
import com.bobocode.petros.bring.context.domain.BeanDefinition;
import com.bobocode.petros.bring.exception.InvalidModifierException;
import com.bobocode.petros.bring.scanner.ConfigurationBeanDefinitionScanner;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.*;

import static com.bobocode.petros.bring.context.domain.BeanScope.SINGLETON;
import static com.bobocode.petros.bring.context.domain.BeanScope.getScopeAsString;
import static com.bobocode.petros.bring.exception.ExceptionMessage.CLASS_IS_NOT_REGISTERED_AS_BEAN_CANDIDATE;
import static com.bobocode.petros.bring.utils.BeanNameUtils.getBeanName;
import static java.lang.String.format;
import static java.util.stream.Collectors.toSet;

/**
 * Implementation of {@link ConfigurationBeanDefinitionScanner}.
 */
public class DefaultConfigurationBeanDefinitionScanner implements ConfigurationBeanDefinitionScanner {

    /**
     * {@inheritDoc}
     *
     * @param classes classes to scan
     * @return collection of bean definitions
     */
    @Override
    public List<BeanDefinition> scan(final Set<Class<?>> classes) {
        if (classes.isEmpty()) {
            return Collections.emptyList();
        }
        final List<BeanDefinition> beanDefinitions = new ArrayList<>();
        final Set<Class<?>> configurationClasses = getConfigurationClasses(classes);
        for (final Class<?> clazz : configurationClasses) {
            final Method[] methods = clazz.getDeclaredMethods();
            for (var method : methods) {
                if (method.isAnnotationPresent(Bean.class)) {
                    checkMethodModifier(method.getModifiers());
                    final BeanDefinition beanDefinition = new BeanDefinition();
                    beanDefinition.setScope(getScopeAsString(SINGLETON));
                    beanDefinition.setBeanName(getBeanName(method));
                    beanDefinition.setBeanClass(method.getReturnType());
                    final Class<?>[] parameterTypes = method.getParameterTypes();
                    if (parameterTypes.length >= 1) {
                        final Map<String, Object> dependencies = beanDefinition.getDependencies();
                        for (final Class<?> parameterType : parameterTypes) {
                            final boolean isRegisteredAsComponent = isRegisteredAsComponent(parameterType);
                            String parameterTypeName;
                            if (isRegisteredAsComponent) {
                                parameterTypeName = getBeanName(parameterType);
                            } else {
                                final boolean isRegisteredAsBeanCandidate = isRegisteredInCurrentClass(methods, parameterType) || isRegisteredGlobally(configurationClasses, parameterType);
                                if (!isRegisteredAsBeanCandidate) {
                                    throw new IllegalArgumentException(format(CLASS_IS_NOT_REGISTERED_AS_BEAN_CANDIDATE, parameterType.getName()));
                                }
                                parameterTypeName = method.getName();
                            }
                            dependencies.put(parameterTypeName, parameterType);
                        }
                    }
                    beanDefinitions.add(beanDefinition);
                }
            }
        }
        return beanDefinitions;
    }

    /**
     * Filters out configuration classes from a set of all classes from a root package.
     *
     * @param classes classes to filter
     * @return set of configuration classes
     */
    private Set<Class<?>> getConfigurationClasses(final Set<Class<?>> classes) {
        return classes.stream()
                .filter(clazz -> clazz.isAnnotationPresent(Configuration.class))
                .collect(toSet());
    }

    /**
     * Checks whether a class is registered in any of the given configuration classes.
     *
     * @param classes classes to filter
     * @param type    class that is supposed to be found among the given set of classes
     * @return true if class is registered in one of a given configuration classes or false otherwise
     */
    private boolean isRegisteredGlobally(final Set<Class<?>> classes, final Class<?> type) {
        return classes.stream()
                .flatMap(clazz -> Arrays.stream(clazz.getDeclaredMethods()))
                .anyMatch(method -> method.getReturnType().isAssignableFrom(type));
    }

    /**
     * Checks whether a class is registered as a method in a configuration class that is currently under scanning.
     *
     * @param methods methods of a current configuration class
     * @param type    class that is supposed to be found
     * @return true if class is registered or false otherwise
     */
    private boolean isRegisteredInCurrentClass(final Method[] methods, final Class<?> type) {
        return Arrays.stream(methods).anyMatch(method -> method.getReturnType().isAssignableFrom(type));
    }

    /**
     * Checks whether a class is annotated with one of the following annotations: @Component, @Service, @Repository.
     *
     * @param type class that is under a check
     * @return true if the class is annotated via one of the given annotations or false otherwise
     */
    private boolean isRegisteredAsComponent(final Class<?> type) {
        return type.isAnnotationPresent(Component.class)
                || type.isAnnotationPresent(Service.class)
                || type.isAnnotationPresent(Repository.class);
    }

    /**
     * Checks whether a method modifier is private and throws exception if the condition is true.
     *
     * @param modifiers method modifiers
     */
    public static void checkMethodModifier(final int modifiers) {
        if (Modifier.isPrivate(modifiers)) {
            throw new InvalidModifierException("Method, annotated with @Bean annotation must not be private!");
        }
    }
}
