package com.bobocode.petros.bring.scanner.mocks;

import com.bobocode.petros.bring.context.domain.BeanReference;
import com.bobocode.petros.bring.factory.postprocessor.BeanPostProcessor;

public class MorningBeanPostProcessor implements BeanPostProcessor {
    @Override
    public BeanReference postProcessBeforeInitialization(BeanReference beanReference) {
        return null;
    }

    @Override
    public BeanReference postProcessAfterInitialization(BeanReference beanReference) {
        return null;
    }
}
