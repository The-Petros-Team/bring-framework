package com.bobocode.petros.bring.registry;

import com.bobocode.petros.bring.context.domain.BeanDefinition;
import com.bobocode.petros.bring.exception.NoSuchBeanDefinitionException;
import com.bobocode.petros.bring.exception.NotUniqueBeanNameException;
import lombok.extern.slf4j.Slf4j;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

import static com.bobocode.petros.bring.exception.ExceptionMessage.*;
import static com.bobocode.petros.bring.utils.BeanUtils.validateBeanName;

/**
 * Implementation of {@link BeanDefinitionRegistry}.
 */
@Slf4j
public class DefaultBeanDefinitionRegistry implements BeanDefinitionRegistry {

    private final Map<String, BeanDefinition> beanDefinitions = new ConcurrentHashMap<>();

    private DefaultBeanDefinitionRegistry() {
    }

    /**
     * {@inheritDoc}
     *
     * @param beanName       name of a bean definition
     * @param beanDefinition bean definition object
     */
    @Override
    public void registerBeanDefinition(final String beanName, final BeanDefinition beanDefinition) {
        validateBeanName(beanName);
        Objects.requireNonNull(beanDefinition, NULL_BEAN_DEFINITION);
        if (beanDefinitions.containsKey(beanName)) {
            throw new NotUniqueBeanNameException(String.format(BEAN_NAME_IS_NOT_UNIQUE, beanName));
        }
        beanDefinitions.put(beanName, beanDefinition);
        log.debug("Registered bean definition: {}", beanDefinition);
    }

    /**
     * {@inheritDoc}
     *
     * @param beanName name of a bean definition that is supposed to be removed
     */
    @Override
    public void remove(final String beanName) {
        validateBeanName(beanName);
        if (!beanDefinitions.containsKey(beanName)) {
            throw new NoSuchBeanDefinitionException(String.format(NO_SUCH_BEAN_DEFINITION, beanName));
        }
        final BeanDefinition removedBeanDefinition = beanDefinitions.remove(beanName);
        log.debug("Removed bean definition: {}", removedBeanDefinition);
    }

    /**
     * {@inheritDoc}
     *
     * @param beanName name of a bean definition that is supposed to be retrieved
     * @return bean definition
     */
    @Override
    public BeanDefinition getBeanDefinition(final String beanName) {
        validateBeanName(beanName);
        if (!beanDefinitions.containsKey(beanName)) {
            throw new NoSuchBeanDefinitionException(String.format(NO_SUCH_BEAN_DEFINITION, beanName));
        }
        return beanDefinitions.get(beanName);
    }

    @Override
    public Collection<BeanDefinition> getAllBeanDefinitions() {
        Collection<BeanDefinition> definitionCollection = beanDefinitions.values();
        if (definitionCollection.isEmpty()) {
            return Collections.emptyList();
        }
        return definitionCollection;
    }

    /**
     * {@inheritDoc}
     *
     * @param beanName name of a bean definition
     * @return true, if bean definition exists in registry or false otherwise
     */
    @Override
    public boolean containsBeanDefinition(final String beanName) {
        validateBeanName(beanName);
        return beanDefinitions.containsKey(beanName);
    }

    /**
     * Accessor that provides a registry instance that is ready to use.
     *
     * @return instance of {@link BeanDefinitionRegistry}
     */
    public static BeanDefinitionRegistry getInstance() {
        return BeanDefinitionRegistryHolder.registry();
    }

    /**
     * {@link BeanDefinitionRegistry} instance holder.
     */
    private enum BeanDefinitionRegistryHolder {

        ;

        private static final BeanDefinitionRegistry REGISTRY = new DefaultBeanDefinitionRegistry();

        BeanDefinitionRegistryHolder() {
        }

        static BeanDefinitionRegistry registry() {
            return REGISTRY;
        }
    }
}
