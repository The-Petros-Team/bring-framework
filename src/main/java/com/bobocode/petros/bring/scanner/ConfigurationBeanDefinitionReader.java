package com.bobocode.petros.bring.scanner;

public interface ConfigurationBeanDefinitionReader {

    <T> void registerBeanDefinition(final Class<T> beanClass);

    <T> void registerBeanDefinition(final String beanName, final Class<T> beanClass);

}
