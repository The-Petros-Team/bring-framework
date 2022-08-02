package com.bobocode.petros.bring.scanner.impl;

import com.bobocode.petros.bring.context.aware.ApplicationContextAware;
import com.bobocode.petros.bring.context.aware.injector.ApplicationContextInjector;
import com.bobocode.petros.bring.factory.postprocessor.BeanPostProcessor;
import com.bobocode.petros.bring.scanner.BeanPostProcessorScanner;
import com.bobocode.petros.bring.utils.ScanningUtils;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Objects;
import java.util.Set;

/**
 * Implementation of {@link BeanPostProcessorScanner}.
 */
@Slf4j
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
                .filter(Objects::nonNull)
                .filter(cl -> !cl.isInterface())
                .filter(BeanPostProcessor.class::isAssignableFrom)
                .map(this::createInstance)
                .toList();
    }

    @SneakyThrows
    private BeanPostProcessor createInstance(Class<?> targetClass) {
        var beanPostProcessor = targetClass.getConstructor().newInstance();
        var applicationContextInjector = ApplicationContextInjector.getInstance();
        if (ScanningUtils.isAwareClass(beanPostProcessor, ApplicationContextAware.class)) {
            applicationContextInjector.inject(beanPostProcessor);
        }
        log.debug("Created '{}' post processor", beanPostProcessor.getClass().getName());
        return (BeanPostProcessor) beanPostProcessor;
    }
}
