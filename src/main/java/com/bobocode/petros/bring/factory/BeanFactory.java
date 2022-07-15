package com.bobocode.petros.bring.factory;

public interface BeanFactory {

    <T> T getBean(final Class<T> requiredType);

    <T> T getBean(final String name, final Class<T> requiredType);

    boolean isSingleton(final String name);

    boolean containsBean(final String name);

    boolean containsBeanDefinition(final String beanName);

    String[] getBeanDefinitionNames();

}
