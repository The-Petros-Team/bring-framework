package com.bobocode.petros.bring.factory.postprocessor;

import com.bobocode.petros.bring.context.domain.BeanReference;

/**
 * Basic interface that allows to link post processors with each other.
 */
public interface BeanPostProcessorContainer {

    /**
     * Launches a chain of post processors for a single bean reference.
     *
     * @param beanReference bean reference to process
     * @return processed bean reference
     */
    BeanReference process(final BeanReference beanReference);

}
