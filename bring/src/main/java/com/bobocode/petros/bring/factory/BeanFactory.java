package com.bobocode.petros.bring.factory;

import com.bobocode.petros.bring.context.domain.BeanDefinition;
import com.bobocode.petros.bring.context.domain.BeanReference;

import java.util.Map;

/**
 * Basic interface that provide contract information about
 * how beanFactory implementation should work.
 */
public interface BeanFactory {

    /**
     * This method using to get all beans. It`s creating beans undercover from bean definition
     * and put it into a map.
     * @return Map where key parameter it`s {@link String} bean name and value parameter
     * it`s {@link BeanReference}
     */
    Map<String, BeanReference> getAllBeanReferences();


    /**
     * This method using to get NEW bean by {@link BeanDefinition}
     * @param beanDefinition
     * @return {@link BeanReference}
     */
    BeanReference createBeanReference(BeanDefinition beanDefinition);
}
