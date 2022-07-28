package com.bobocode.petros.bring.factory.postprocessor;

import com.bobocode.petros.bring.context.domain.BeanReference;
import com.bobocode.petros.bring.scanner.BeanPostProcessorScanner;
import com.bobocode.petros.bring.scanner.impl.DefaultBeanPostProcessorScanner;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Set;

/**
 * Implementation of {@link BeanPostProcessorContainer}.
 */
public class DefaultBeanPostProcessorContainer implements BeanPostProcessorContainer {

    private BeanPostProcessorScanner beanPostProcessorScanner;
    private List<BeanPostProcessor> postProcessors;

    public DefaultBeanPostProcessorContainer(Set<Class<?>> classes) {
        this.beanPostProcessorScanner = new DefaultBeanPostProcessorScanner();
        this.postProcessors = Objects.requireNonNull(this.beanPostProcessorScanner.scan(classes))
                .stream()
                .sorted(Comparator.comparing(BeanPostProcessor::getOrder))
                .toList();
    }

    /**
     * {@inheritDoc}
     *
     * @param beanReference bean reference to process
     * @return processed bean reference
     */
    @Override
    public BeanReference process(BeanReference beanReference) {
        this.postProcessors.forEach(postProcessor -> postProcessor.postProcessBeforeInitialization(beanReference));
        this.postProcessors.forEach(postProcessor -> postProcessor.postProcessAfterInitialization(beanReference));
        return beanReference;
    }
}
