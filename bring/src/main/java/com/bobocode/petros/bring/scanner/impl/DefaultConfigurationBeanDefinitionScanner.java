package com.bobocode.petros.bring.scanner.impl;

import com.bobocode.petros.bring.annotation.Bean;
import com.bobocode.petros.bring.context.domain.BeanDefinition;
import com.bobocode.petros.bring.context.domain.Dependency;
import com.bobocode.petros.bring.exception.InvalidModifierException;
import com.bobocode.petros.bring.scanner.ConfigurationBeanDefinitionScanner;
import com.bobocode.petros.bring.utils.BeanNameUtils;
import com.bobocode.petros.bring.utils.ScanningUtils;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static com.bobocode.petros.bring.context.domain.BeanScope.SINGLETON;
import static com.bobocode.petros.bring.context.domain.BeanScope.getScopeAsString;
import static com.bobocode.petros.bring.exception.ExceptionMessage.METHOD_MUST_NOT_BE_PRIVATE;

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
        final Set<Class<?>> configurationClasses = ScanningUtils.getConfigurationClasses(classes);
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
                        ScanningUtils.handleInterfaceDuringBeanDefinitionCreation(classes, method.getName(), beanDefinition, returnType);
                    }
                    beanDefinition.setBeanClass(returnType);
                    final Class<?>[] parameterTypes = method.getParameterTypes();
                    log.debug("Found {} parameter types for {} method", parameterTypes.length, method.getName());
                    if (parameterTypes.length >= 1) {
                        final Map<String, Dependency> dependencies = beanDefinition.getDependencies();
                        for (final Class<?> parameterType : parameterTypes) {
                            final Dependency dependency = new Dependency();
                            Class<?> implementation = parameterType;
                            dependency.setImplementation(implementation);
                            if (parameterType.isInterface()) {
                                final List<Class<?>> implementations = ScanningUtils.findImplementations(classes, parameterType);
                                implementation = implementations.iterator().next();
                                log.debug(
                                        "Found {} implementation(s) for resolved dependency '{}' of bean definition: {}",
                                        implementations.size(), parameterType.getName(), beanDefinition.getBeanName()
                                );
                                ScanningUtils.checkImplementations(implementations.size(), parameterType.getName());
                                dependency.setInterfaceClass(parameterType);
                                dependency.setImplementation(implementation);
                            }
                            final boolean isRegisteredAsComponent = ScanningUtils.isRegisteredAsComponent(implementation);
                            String parameterTypeName;
                            if (isRegisteredAsComponent) {
                                log.debug("Class '{}' is registered as a component", implementation.getName());
                                parameterTypeName = BeanNameUtils.getBeanName(implementation);
                            } else {
                                final boolean isRegisteredAsBeanCandidate =
                                        isRegisteredInCurrentClass(methods, implementation)
                                                || ScanningUtils.isRegisteredGlobally(configurationClasses, implementation);
                                ScanningUtils.checkBeanCandidate(implementation, isRegisteredAsBeanCandidate);
                                log.debug("Class '{}' is registered in Java configuration", implementation.getName());
                                parameterTypeName = method.getName();
                            }
                            dependencies.put(parameterTypeName, dependency);
                        }
                    }
                    beanDefinitions.add(beanDefinition);
                }
            }
        }
        return beanDefinitions;
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
