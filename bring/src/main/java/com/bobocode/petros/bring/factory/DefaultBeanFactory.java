package com.bobocode.petros.bring.factory;

import com.bobocode.petros.bring.context.domain.BeanDefinition;
import com.bobocode.petros.bring.context.domain.BeanReference;
import com.bobocode.petros.bring.context.domain.BeanScope;
import com.bobocode.petros.bring.exception.NoSuchBeanDefinitionException;
import com.bobocode.petros.bring.factory.postprocessor.BeanPostProcessorContainer;
import com.bobocode.petros.bring.registry.BeanDefinitionRegistry;
import lombok.SneakyThrows;

import java.util.Map;
import java.util.stream.Collectors;

import static com.bobocode.petros.bring.exception.ExceptionMessage.NO_INTERFACE_IMPLEMENTATION;
import static com.bobocode.petros.bring.exception.ExceptionMessage.NULL_BEAN_DEFINITION;
import static java.util.Objects.requireNonNull;

public class DefaultBeanFactory implements BeanFactory {

    private final BeanDefinitionRegistry beanDefinitionRegistry;
    private final BeanPostProcessorContainer beanPostProcessorContainer;

    public DefaultBeanFactory(BeanDefinitionRegistry beanDefinitionRegistry, BeanPostProcessorContainer beanPostProcessorContainer) {
        this.beanDefinitionRegistry = beanDefinitionRegistry;
        this.beanPostProcessorContainer = beanPostProcessorContainer;
    }

    @Override
    public Map<String, BeanReference> getAllBeanReferences() {
        return beanDefinitionRegistry.getAllBeanDefinitions().stream()
                .collect(Collectors.toMap(BeanDefinition::getBeanName, this::createBeanReference));
    }

    @Override
    @SneakyThrows
    public BeanReference createBeanReference(final BeanDefinition beanDefinition) {
        requireNonNull(beanDefinition, NULL_BEAN_DEFINITION);
        if (beanDefinition.isInterface()) {
            var beanDefinitionImpl = beanDefinition.getImplementations().values()
                    .stream()
                    .findFirst()
                    .orElseThrow(() -> new NoSuchBeanDefinitionException(String.format(NO_INTERFACE_IMPLEMENTATION,
                            ((Class<?>) beanDefinition.getBeanClass()).getSimpleName())));
            var instance = ((Class<?>) beanDefinitionImpl).getDeclaredConstructor().newInstance();
            final BeanReference beanReference = new BeanReference(instance, BeanScope.getScopeFromString(beanDefinition.getScope()));
            return this.beanPostProcessorContainer.process(beanReference);
        } else {
            var instance = ((Class<?>) beanDefinition.getBeanClass()).getDeclaredConstructor().newInstance();
            final BeanReference beanReference = new BeanReference(instance, BeanScope.getScopeFromString(beanDefinition.getScope()));
            return this.beanPostProcessorContainer.process(beanReference);
        }
    }
}
