package com.bobocode.petros.bring.scanner.impl;

import com.bobocode.petros.bring.context.domain.BeanDefinition;
import com.bobocode.petros.bring.scanner.ClassPathBeanDefinitionScanner;
import com.bobocode.petros.bring.utils.BeanNameUtils;
import com.bobocode.petros.bring.utils.ScanningUtils;
import lombok.extern.slf4j.Slf4j;

import java.util.Collections;
import java.util.List;
import java.util.Set;
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

    /**
     * @param classes classes to scan
     * @return collection of bean definitions
     */
    @Override
    public List<BeanDefinition> scan(Set<Class<?>> classes) {
        if (classes.isEmpty()) {
            return Collections.emptyList();
        }
        Set<Class<?>> components = searchForBeansLikeClasses(classes);
        return components.stream()
                .map((Class<?> beanCandidateClass) -> mapToBeanDefinitions(beanCandidateClass, classes))
                .toList();
    }

    /**
     * Search for all classes with annotations from {{@link ScanningUtils#isRegisteredAsComponent}}
     *
     * @param classes classes to scan
     */
    private Set<Class<?>> searchForBeansLikeClasses(Set<Class<?>> classes) {
        return classes.stream()
                .filter(clazz -> !clazz.isAnnotation())
                .filter(ScanningUtils::isRegisteredAsComponent)
                .collect(Collectors.toSet());
    }

    /**
     * The Mapping used in {@link #scan(Set)} which mapped classes to {@link BeanDefinition},
     */
    private BeanDefinition mapToBeanDefinitions(Class<?> beanCandidateClass, Set<Class<?>> allClasses) {
        var beanDefinition = new BeanDefinition();
        beanDefinition.setBeanName(BeanNameUtils.getBeanName(beanCandidateClass));
        beanDefinition.setBeanClass(beanCandidateClass);
        beanDefinition.setScope(getScopeAsString(SINGLETON));
        if (beanCandidateClass.isInterface()) {
            ScanningUtils.handleInterfaceDuringBeanDefinitionCreation(allClasses, beanCandidateClass.getName(), beanDefinition, beanCandidateClass);
        }
        return beanDefinition;
    }
}
