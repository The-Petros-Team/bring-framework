package com.bobocode.petros.bring.context.aware.postprocessor;

import com.bobocode.petros.bring.context.ApplicationContext;
import com.bobocode.petros.bring.context.aware.ApplicationContextAware;
import com.bobocode.petros.bring.context.domain.BeanReference;
import com.bobocode.petros.bring.factory.postprocessor.BeanPostProcessor;
import lombok.Getter;

public class NoOpBeanPostProcessor implements BeanPostProcessor, ApplicationContextAware {

    @Getter
    private ApplicationContext context;

    @Override
    public BeanReference postProcessBeforeInitialization(BeanReference beanReference) {
        return beanReference;
    }

    @Override
    public BeanReference postProcessAfterInitialization(BeanReference beanReference) {
        return beanReference;
    }
}
