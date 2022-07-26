package com.bobocode.petros.bring.scanner.impl;

import com.bobocode.petros.bring.annotation.*;
import com.bobocode.petros.bring.context.domain.BeanDefinition;
import com.bobocode.petros.bring.context.domain.DependencyPair;
import com.bobocode.petros.bring.exception.IllegalBeanDefinitionStateException;
import com.bobocode.petros.bring.exception.InvalidModifierException;
import com.bobocode.petros.bring.scanner.ConfigurationBeanDefinitionScanner;
import com.bobocode.petros.bring.utils.BeanNameUtils;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.*;
import java.util.stream.Collectors;

import static com.bobocode.petros.bring.context.domain.BeanScope.SINGLETON;
import static com.bobocode.petros.bring.context.domain.BeanScope.getScopeAsString;
import static com.bobocode.petros.bring.exception.ExceptionMessage.*;

/**
 * Implementation of {@link ConfigurationBeanDefinitionScanner}.
 */
@Slf4j
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
        log.debug("Found {} configuration classes", configurationClasses.size());
        for (final Class<?> clazz : configurationClasses) {
            final Method[] methods = clazz.getDeclaredMethods();
            for (var method : methods) {
                if (method.isAnnotationPresent(Bean.class)) {
                    checkMethodModifier(method.getModifiers());
                    final BeanDefinition beanDefinition = new BeanDefinition();
                    beanDefinition.setScope(getScopeAsString(SINGLETON));
                    beanDefinition.setBeanName(BeanNameUtils.getBeanName(method));
                    final Class<?> returnType = method.getReturnType();
                    if (returnType.isInterface()) {
                        beanDefinition.setInterface(true);
                        final List<Class<?>> implementations = findImplementations(classes, returnType);
                        log.debug("Found {} implementations for '{}'", implementations.size(), returnType.getName());
                        checkImplementations(implementations.size(), returnType.getName());
                        final Map<String, Object> beanDefinitionImplementations = beanDefinition.getImplementations();
                        implementations.forEach(impl -> beanDefinitionImplementations.put(method.getName(), impl));
                    }
                    beanDefinition.setBeanClass(returnType);
                    final Class<?>[] parameterTypes = method.getParameterTypes();
                    log.debug("Found {} parameter types for {} method", parameterTypes.length, method.getName());
                    if (parameterTypes.length >= 1) {
                        final Map<String, DependencyPair> dependencies = beanDefinition.getDependencies();
                        for (final Class<?> parameterType : parameterTypes) {
                            final DependencyPair pair = new DependencyPair();
                            Class<?> implementation = parameterType;
                            pair.setImplementation(implementation);
                            if (parameterType.isInterface()) {
                                final List<Class<?>> implementations = findImplementations(classes, parameterType);
                                log.debug(
                                        "Found {} implementation(s) for resolved dependency '{}' of bean definition: {}",
                                        implementations.size(), parameterType.getName(), beanDefinition.getBeanName()
                                );
                                checkImplementations(implementations.size(), parameterType.getName());
                                final Iterator<Class<?>> iterator = implementations.iterator();
                                implementation = iterator.next();
                                pair.setInterfaceClass(parameterType);
                                pair.setImplementation(implementation);
                            }
                            final boolean isRegisteredAsComponent = isRegisteredAsComponent(implementation);
                            String parameterTypeName;
                            if (isRegisteredAsComponent) {
                                log.debug("Class '{}' is registered as a component", implementation.getName());
                                parameterTypeName = BeanNameUtils.getBeanName(implementation);
                            } else {
                                final boolean isRegisteredAsBeanCandidate =
                                        isRegisteredInCurrentClass(methods, implementation)
                                                || isRegisteredGlobally(configurationClasses, implementation);
                                checkBeanCandidate(implementation, isRegisteredAsBeanCandidate);
                                log.debug("Class '{}' is registered in Java configuration", implementation.getName());
                                parameterTypeName = method.getName();
                            }
                            dependencies.put(parameterTypeName, pair);
                        }
                    }
                    beanDefinitions.add(beanDefinition);
                }
            }
        }
        return beanDefinitions;
    }

    /**
     * Checks whether a class is registered as a bean candidate. Throws an exception if condition mismatched.
     *
     * @param implementation              class that is not registered properly
     * @param isRegisteredAsBeanCandidate condition to check
     */
    private void checkBeanCandidate(final Class<?> implementation, final boolean isRegisteredAsBeanCandidate) {
        if (!isRegisteredAsBeanCandidate) {
            throw new IllegalArgumentException(String.format(CLASS_IS_NOT_REGISTERED_AS_BEAN_CANDIDATE, implementation.getName()));
        }
    }

    /**
     * Checks whether an interface has no more that a single implementation and throws an exception if yes.
     *
     * @param implementations number of implementations
     * @param type            interface name
     */
    private void checkImplementations(final int implementations, final String type) {
        if (implementations != 1) {
            throw new IllegalBeanDefinitionStateException(String.format(INTERFACE_HAS_MORE_THEN_ONE_IMPLEMENTATION, type));
        }
    }

    /**
     * Returns implementations for a given interface.
     *
     * @param classes     classes to scan
     * @param anInterface an interface which implementations should be found
     * @return list of implementations
     */
    private List<Class<?>> findImplementations(final Set<Class<?>> classes, final Class<?> anInterface) {
        return classes.stream()
                .filter(clazz -> !clazz.isInterface())
                .filter(anInterface::isAssignableFrom)
                .toList();
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
                .collect(Collectors.toSet());
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
    private void checkMethodModifier(final int modifiers) {
        if (Modifier.isPrivate(modifiers)) {
            throw new InvalidModifierException(METHOD_MUST_NOT_BE_PRIVATE);
        }
    }
}
