package com.bobocode.petros.bring.registry.utils;

import com.bobocode.petros.bring.context.domain.BeanDefinition;
import com.bobocode.petros.bring.registry.BeanDefinitionRegistry;

import java.lang.reflect.Field;
import java.util.Map;

public final class RegistryTestUtils {

    private RegistryTestUtils() {}

    public static void configureRegistry(final BeanDefinitionRegistry registry, final Map<String, BeanDefinition> beanDefinitions) {
        try {
            final Class<? extends BeanDefinitionRegistry> registryClass = registry.getClass();
            final Field beanDefinitionsField = registryClass.getDeclaredField("beanDefinitions");
            beanDefinitionsField.setAccessible(true);
            beanDefinitionsField.set(registry, beanDefinitions);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
}
