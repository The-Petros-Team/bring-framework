package com.bobocode.petros.bring.scanner.mocks.postprocessor;

import com.bobocode.petros.bring.context.domain.BeanReference;
import com.bobocode.petros.bring.factory.postprocessor.BeanPostProcessor;

public class EveningBeanPostProcessor implements BeanPostProcessor {
    @Override
    public BeanReference postProcessBeforeInitialization(BeanReference beanReference) {
        return beanReference;
    }

    @Override
    public BeanReference postProcessAfterInitialization(BeanReference beanReference) {
        return beanReference;
    }

    @Override
    public int getOrder() {
        return 0;
    }
}
