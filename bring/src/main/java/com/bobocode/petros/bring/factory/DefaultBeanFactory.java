package com.bobocode.petros.bring.factory;

import com.bobocode.petros.bring.context.domain.BeanDefinition;
import com.bobocode.petros.bring.context.domain.BeanReference;
import com.bobocode.petros.bring.context.domain.BeanScope;
import com.bobocode.petros.bring.exception.NoSuchBeanDefinitionException;
import com.bobocode.petros.bring.registry.BeanDefinitionRegistry;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.stream.Collectors;

import static com.bobocode.petros.bring.exception.ExceptionMessage.NO_INTERFACE_IMPLEMENTATION;
import static com.bobocode.petros.bring.exception.ExceptionMessage.NULL_BEAN_DEFINITION;
import static java.util.Objects.requireNonNull;

@Slf4j
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
        requireNonNull(beanDefinition, NULL_BEAN_DEFINITION);
        BeanReference beanReference = null;
        if (beanDefinition.isInterface()) {
            var beanDefinitionImpl = beanDefinition.getImplementations().values()
                    .stream()
                    .findFirst()
                    .orElseThrow(() -> new NoSuchBeanDefinitionException(String.format(NO_INTERFACE_IMPLEMENTATION,
                            ((Class<?>) beanDefinition.getBeanClass()).getSimpleName())));
            var instance = ((Class<?>) beanDefinitionImpl).getDeclaredConstructor().newInstance();
            beanReference = new BeanReference(instance, BeanScope.getScopeFromString(beanDefinition.getScope()));
        } else {
            var instance = ((Class<?>) beanDefinition.getBeanClass()).getDeclaredConstructor().newInstance();
            beanReference = new BeanReference(instance, BeanScope.getScopeFromString(beanDefinition.getScope()));
        }
        log.debug("Created bean reference {}", beanReference);
        return beanReference;
    }
}
