package com.bobocode.petros.bring.factory.postprocessor;

import com.bobocode.petros.bring.annotation.Autowired;
import com.bobocode.petros.bring.context.ApplicationContext;
import com.bobocode.petros.bring.context.aware.ApplicationContextAware;
import com.bobocode.petros.bring.context.domain.BeanReference;
import com.bobocode.petros.bring.exception.NoUniqueBeanException;
import lombok.SneakyThrows;

import java.lang.reflect.Field;

public class FieldInjectionBeanPostProcessor implements BeanPostProcessor, ApplicationContextAware {

    private ApplicationContext context;

    //TODO: make possible to inject collection of beans, new method in ApplicationContext that return all beans by bean type ?
    @Override
    @SneakyThrows
    public BeanReference postProcessBeforeInitialization(BeanReference beanReference) {
        var beanObject = beanReference.getBeanObject();
        for (Field field : beanObject.getClass().getDeclaredFields()) {
            if (field.isAnnotationPresent(Autowired.class)) {
                field.setAccessible(true);
                Object objectToAutowire;
                try {
                    objectToAutowire = context.getBean(field.getType());
                } catch (NoUniqueBeanException exception) {
                    objectToAutowire = context.getBean(field.getName(), field.getType());
                }
                field.set(beanObject, objectToAutowire);
            }
        }
        return beanReference;
    }

    @Override
    public BeanReference postProcessAfterInitialization(BeanReference beanReference) {
        return beanReference;
    }

    @Override
    public int getOrder() {
        return Integer.MIN_VALUE;
    }
}
