package com.bobocode.petros.bring.scanner.impl;

import com.bobocode.petros.bring.factory.postprocessor.BeanPostProcessor;
import com.bobocode.petros.bring.scanner.BeanPostProcessorScanner;
import lombok.SneakyThrows;

import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

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
        return classes.stream()
                .flatMap(Stream::ofNullable)
                .filter(BeanPostProcessor.class::isAssignableFrom)
                .map(this::createInstance)
                .toList();
    }

    @SneakyThrows
    private BeanPostProcessor createInstance(Class<?> targetClass) {
        var beanPostProcessor = targetClass.getConstructor().newInstance();
        return ((BeanPostProcessor) beanPostProcessor);
    }
}
