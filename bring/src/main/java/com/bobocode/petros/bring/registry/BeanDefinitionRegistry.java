package com.bobocode.petros.bring.registry;

import com.bobocode.petros.bring.context.domain.BeanDefinition;

/**
 * Registry stores bean definitions that are created and configured.
 * Please note, that registry holds bean definitions that are not final objects (beans).
 * Registry can be considered as an intermediate storage for bean definitions that passed creation and
 * configuration stages.
 */
public interface BeanDefinitionRegistry {

    /**
     * Allows to register a bean definition in registry.
     *
     * @param beanName name of a bean definition
     * @param beanDefinition bean definition object
     */
    void registerBeanDefinition(final String beanName, final BeanDefinition beanDefinition);

    /**
     * Removes bean definition from registry.
     *
     * @param beanName name of a bean definition that is supposed to be removed
     */
    void remove(final String beanName);

    /**
     * Retrieves bean definition from registry.
     *
     * @param beanName name of a bean definition that is supposed to be retrieved
     * @return bean definition
     */
    BeanDefinition getBeanDefinition(final String beanName);

    /**
     * Checks whether bean definition is registered in registry.
     *
     * @param beanName name of a bean definition
     * @return true, if bean definition exists in registry or false otherwise
     */
    boolean containsBeanDefinition(final String beanName);

}
