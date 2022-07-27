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
     */
    BeanReference postProcess(BeanReference beanReference);
}
