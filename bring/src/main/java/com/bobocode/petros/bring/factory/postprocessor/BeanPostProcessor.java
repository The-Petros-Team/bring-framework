package com.bobocode.petros.bring.factory.postprocessor;

import com.bobocode.petros.bring.context.domain.BeanReference;

/**
 * BeanPostProcessor allows custom modification of bean objects in {@link BeanReference}  —
 * for example, checking for marker annotations or wrapping beans with proxies.
 */
public interface BeanPostProcessor {
    /**
     * Apply this method to the given {@link BeanReference} instance
     * to modify bean object
     *
     * @param beanReference the new bean reference
     * @return configured bean reference
     */
    BeanReference postProcessBeforeInitialization(BeanReference beanReference);

    /**
     * Allows to additionally configure an initialized bean, for example, wrapping a bean with proxy etc.
     *
     * @param beanReference bean reference
     * @return configured bean reference
     */
    BeanReference postProcessAfterInitialization(BeanReference beanReference);

    /**
     * Allows to run bean post processor in a specific order.
     * In case this method is not overriden, post processor is registered automatically as the last one.
     */
    default int getOrder() {
        return Integer.MAX_VALUE;
    }
    
}
