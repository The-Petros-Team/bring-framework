package com.bobocode.petros.bring.scanner;

import com.bobocode.petros.bring.factory.postprocessor.BeanPostProcessor;

import java.util.List;
import java.util.Set;

/**
 * Allows to scan all implementations of {@link BeanPostProcessor}.
 */
public interface BeanPostProcessorScanner {

    /**
     * Applies scanning against the given set of classes.
     *
     * @param classes classes to scan
     * @return list of bean post processors
     */
    List<BeanPostProcessor> scan(final Set<Class<?>> classes);

}
