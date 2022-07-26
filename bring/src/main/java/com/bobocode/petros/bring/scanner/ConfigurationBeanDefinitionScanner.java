package com.bobocode.petros.bring.scanner;

import com.bobocode.petros.bring.context.domain.BeanDefinition;

import java.util.List;
import java.util.Set;

/**
 * Scans a given set of classes and resolves those that are supposed to become beans, mandatory metadata is stored in
 * {@link BeanDefinition} and put to the result collection.
 */
public interface ConfigurationBeanDefinitionScanner {

    /**
     * Performs package scanning in order to resolve configuration classes and create bean definitions for each of them.
     *
     * @param classes classes to scan
     * @return collection of bean definitions
     */
    List<BeanDefinition> scan(final Set<Class<?>> classes);

}
