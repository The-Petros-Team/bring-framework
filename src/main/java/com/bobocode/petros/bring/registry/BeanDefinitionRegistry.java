package com.bobocode.petros.bring.registry;

import com.bobocode.petros.bring.factory.BeanDefinition;

public interface BeanDefinitionRegistry {

    void registerBeanDefinition(final String beanName, final BeanDefinition beanDefinition);

    void remove(final String beanName);

    BeanDefinition getBeanDefinition(final String beanName);

    boolean containsBeanDefinition(final String beanName);

}
