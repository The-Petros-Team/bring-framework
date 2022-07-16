package com.bobocode.petros.bring.registry;

import com.bobocode.petros.bring.context.domain.BeanDefinition;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class DefaultBeanDefinitionRegistry implements BeanDefinitionRegistry {

    private static final Map<String, BeanDefinition> BEAN_DEFINITIONS = new ConcurrentHashMap<>();

    @Override
    public void registerBeanDefinition(String beanName, BeanDefinition beanDefinition) {

    }

    @Override
    public void remove(String beanName) {

    }

    @Override
    public BeanDefinition getBeanDefinition(String beanName) {
        return null;
    }

    @Override
    public boolean containsBeanDefinition(String beanName) {
        return false;
    }
}
