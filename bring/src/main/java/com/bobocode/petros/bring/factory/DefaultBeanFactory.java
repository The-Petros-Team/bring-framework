package com.bobocode.petros.bring.factory;

import com.bobocode.petros.bring.context.domain.BeanDefinition;
import com.bobocode.petros.bring.context.domain.BeanReference;
import com.bobocode.petros.bring.context.domain.BeanScope;
import com.bobocode.petros.bring.registry.BeanDefinitionRegistry;
import lombok.SneakyThrows;

import java.util.Map;
import java.util.stream.Collectors;

import static java.util.Objects.requireNonNull;

public class DefaultBeanFactory implements BeanFactory {
    private final BeanDefinitionRegistry beanDefinitionRegistry;

    public DefaultBeanFactory(BeanDefinitionRegistry beanDefinitionRegistry) {
        this.beanDefinitionRegistry = beanDefinitionRegistry;
    }

    @Override
    public Map<String, BeanReference> getAllBeanReferences() {
        return beanDefinitionRegistry.getAllBeanDefinitions().stream()
                .collect(Collectors.toMap(BeanDefinition::getBeanName, this::createBeanReference));
    }

    @Override
    @SneakyThrows
    public BeanReference createBeanReference(final BeanDefinition beanDefinition) {
        requireNonNull(beanDefinition);
        var instance = ((Class<?>) beanDefinition.getBeanClass()).getDeclaredConstructor().newInstance();
        return new BeanReference(instance, BeanScope.getScopeFromString(beanDefinition.getScope()));
    }
}
