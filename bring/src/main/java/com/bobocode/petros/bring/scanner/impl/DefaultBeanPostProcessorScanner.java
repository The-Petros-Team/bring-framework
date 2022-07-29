package com.bobocode.petros.bring.scanner.impl;

import com.bobocode.petros.bring.factory.postprocessor.BeanPostProcessor;
import com.bobocode.petros.bring.scanner.BeanPostProcessorScanner;

import java.util.List;
import java.util.Set;

/**
 * Implementation of {@link BeanPostProcessorScanner}.
 */
public class DefaultBeanPostProcessorScanner implements BeanPostProcessorScanner {

    /**
     * {@inheritDoc}
     *
     * @param classes classes to scan
     * @return list of bean post processors
     */
    @Override
    public List<BeanPostProcessor> scan(Set<Class<?>> classes) {
        return null;
    }
}
