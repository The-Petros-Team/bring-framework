package com.bobocode.petros.bring.registry;

import com.bobocode.petros.bring.context.domain.BeanDefinition;
import com.bobocode.petros.bring.exception.NoSuchBeanException;
import com.bobocode.petros.bring.exception.NotUniqueBeanNameException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

import static com.bobocode.petros.bring.exception.ExceptionMessage.*;
import static com.bobocode.petros.bring.utils.BeanUtils.validateBeanName;

/**
 * Implementation of {@link BeanDefinitionRegistry}.
 */
public class DefaultBeanDefinitionRegistry implements BeanDefinitionRegistry {

    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultBeanDefinitionRegistry.class);

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
        LOGGER.debug("Registered bean definition: {}", beanDefinition);
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
            throw new NoSuchBeanException(String.format(NO_SUCH_BEAN, beanName));
        }
        final BeanDefinition removedBeanDefinition = beanDefinitions.remove(beanName);
        LOGGER.debug("Removed bean definition: {}", removedBeanDefinition);
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
            throw new NoSuchBeanException(String.format(NO_SUCH_BEAN, beanName));
        }
        return beanDefinitions.get(beanName);
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
