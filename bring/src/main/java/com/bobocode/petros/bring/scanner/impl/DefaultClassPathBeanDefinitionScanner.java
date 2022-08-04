package com.bobocode.petros.bring.scanner.impl;

import com.bobocode.petros.bring.annotation.Component;
import com.bobocode.petros.bring.annotation.Repository;
import com.bobocode.petros.bring.annotation.Service;
import com.bobocode.petros.bring.context.domain.BeanDefinition;
import com.bobocode.petros.bring.scanner.ClassPathBeanDefinitionScanner;
import com.bobocode.petros.bring.utils.BeanNameUtils;
import com.bobocode.petros.bring.utils.ScanningUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static com.bobocode.petros.bring.context.domain.BeanScope.SINGLETON;
import static com.bobocode.petros.bring.context.domain.BeanScope.getScopeAsString;

/**
 * Implementation of {@link DefaultClassPathBeanDefinitionScanner}
 *
 * @author YOliinyk
 * @version 0.0.1
 */
public class DefaultClassPathBeanDefinitionScanner implements ClassPathBeanDefinitionScanner {

    private final List<Class<? extends Annotation>> beanTypes = List.of(
            Component.class,
            Service.class,
            Repository.class
    );

    /**
     * @param classes classes to scan
     * @return collection of bean definitions
     */
    @Override
    public List<BeanDefinition> scan(Set<Class<?>> classes) {
        if (classes.isEmpty()) {
            return Collections.emptyList();
        }

        // find annotated classes from all classes
        final Set<Class<?>> componentClasses = ScanningUtils.findComponents(classes);
        // as a result we have a collection of classes (implementations)
        // so as the next step we should analyze its interfaces
        // if class implements an interface, then set beanObject to interface
        // then set implementation and isInterface = true
        for (final Class<?> componentClass : componentClasses) {
            final Set<Type> interfaces = ScanningUtils.getInterfaces(componentClass);

            final int size = interfaces.size();
            final Optional<? extends Class<?>> anInterface = getAssignableInterface(interfaces, componentClass);
            
        }
        // check if class has dependencies and collect them

        Set<Class<?>> components = searchForBeansLikeClasses(classes);
        return components.stream()
                .map(this::mapToBeanDefinitions)
                .toList();
    }

    private Optional<? extends Class<?>> getAssignableInterface(final Set<Type> interfaces, final Class<?> componentClass) {
        return interfaces.stream()
                .map(type -> (Class<?>) type)
                .filter(interfaze -> interfaze.isAssignableFrom(componentClass))
                .findAny();
    }

    /**
     * Search for all classes with annotations from {@link #beanTypes}
     *
     * @param classes classes to scan
     */
    private Set<Class<?>> searchForBeansLikeClasses(Set<Class<?>> classes) {
        return classes.stream()
                .filter(clazz -> !clazz.isAnnotation())
                .filter(allBeansLikeClassesPredicate())
                .collect(Collectors.toSet());
    }

    /**
     * Predicate for filter in {@link #searchForBeansLikeClasses(Set)},
     * which filter all classes with annotation from {@link #beanTypes} list
     */
    private Predicate<Class<?>> allBeansLikeClassesPredicate() {
        return clazz -> beanTypes.stream().anyMatch(
                bType -> Arrays.stream(clazz.getAnnotations())
                        .anyMatch(annotation -> annotation.annotationType().isAssignableFrom(bType)));
    }

    /**
     * The Mapping used in {@link #scan(Set)} which mapped classes to {@link BeanDefinition},
     */
    private BeanDefinition mapToBeanDefinitions(Class<?> beanCandidateClass) {
        var beanDefinition = new BeanDefinition();
        beanDefinition.setBeanName(BeanNameUtils.getBeanName(beanCandidateClass));
        beanDefinition.setBeanClass(beanCandidateClass);
        beanDefinition.setScope(getScopeAsString(SINGLETON));
        return beanDefinition;
    }
}
