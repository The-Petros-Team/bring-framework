package com.bobocode.petros.bring.factory;

import com.bobocode.petros.bring.context.domain.BeanDefinition;
import com.bobocode.petros.bring.context.domain.BeanReference;
import com.bobocode.petros.bring.registry.BeanDefinitionRegistry;
import lombok.SneakyThrows;

import java.util.Map;

public class DefaultBeanFactory implements BeanFactory {
    private final BeanDefinitionRegistry beanDefinitionRegistry;

    public DefaultBeanFactory(BeanDefinitionRegistry beanDefinitionRegistry) {
        this.beanDefinitionRegistry = beanDefinitionRegistry;
    }

    @Override
    public Map<String, BeanReference> getAllBeanReferences() {
        return null;
    }

    @Override
    public BeanReference createBeanReference(BeanDefinition beanDefinition) {
        return null;
    }

    @SneakyThrows
    private Object createBeanUsingDefaultConstructor(BeanDefinition beanDefinition) {
        return beanDefinition.getBeanClass().getClass().getConstructor().newInstance();
    }

    private Object processBeanWithInnerBean(BeanDefinition beanDefinition) {
        return null;
    }

}
