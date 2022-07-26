package com.bobocode.petros.bring.scanner.impl;

import com.bobocode.petros.bring.annotation.Component;
import com.bobocode.petros.bring.annotation.Repository;
import com.bobocode.petros.bring.annotation.Service;
import com.bobocode.petros.bring.context.domain.BeanDefinition;
import com.bobocode.petros.bring.scanner.ClassPathBeanDefinitionScanner;
import com.bobocode.petros.bring.utils.BeanNameUtils;
import lombok.extern.slf4j.Slf4j;

import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
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
@Slf4j
public class DefaultClassPathBeanDefinitionScanner implements ClassPathBeanDefinitionScanner {

    // ToDo move to properties file
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
        Set<Class<?>> components = searchForBeansLikeClasses(classes);
        return components.stream()
                .map(this::mapToBeanDefinitions)
                .toList();
    }

    /**
     * Search for all classes with annotations from {@link #beanTypes}
     *
     * @param classes classes to scan
     */
    private Set<Class<?>> searchForBeansLikeClasses(Set<Class<?>> classes) {
        return classes.stream()
                .filter(allBeansLikeClassesPredicate())
                .collect(Collectors.toSet());
    }

    /**
     * Predicate for filter in {@link #searchForBeansLikeClasses(Set)},
     * which filter all classes with annotation from {@link #beanTypes}
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
