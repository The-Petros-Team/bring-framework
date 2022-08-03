package com.bobocode.petros.bring.factory.postprocessor;

import com.bobocode.petros.bring.annotation.Autowired;
import com.bobocode.petros.bring.context.ApplicationContext;
import com.bobocode.petros.bring.context.aware.ApplicationContextAware;
import com.bobocode.petros.bring.context.domain.BeanReference;
import com.bobocode.petros.bring.exception.NoUniqueAutowiredConstructorException;
import lombok.SneakyThrows;

import java.lang.reflect.Constructor;
import java.util.Arrays;

import static com.bobocode.petros.bring.exception.ExceptionMessage.NO_UNIQUE_AUTOWIRED_CONSTRUCTOR;
import static java.util.Objects.isNull;

public class ConstructorInjectionBeanPostProcessor implements BeanPostProcessor, ApplicationContextAware {

    private ApplicationContext context;

    @Override
    @SneakyThrows
    public BeanReference postProcessBeforeInitialization(BeanReference beanReference) {
        var beanObject = beanReference.getBeanObject();
        var declaredConstructors = beanObject.getClass().getDeclaredConstructors();
        var constructorsCount = declaredConstructors.length;

        if (constructorsCount == 0) {
            return beanReference;
        }

        Constructor<?> beanConstructor = null;

        if (constructorsCount > 1) {
            var autowiredConstructors = Arrays.stream(declaredConstructors)
                    .filter(c -> c.isAnnotationPresent(Autowired.class))
                    .toList();
            if (autowiredConstructors.size() > 1) {
                throw new NoUniqueAutowiredConstructorException(NO_UNIQUE_AUTOWIRED_CONSTRUCTOR);
            }
            if (autowiredConstructors.size() == 1) {
                beanConstructor = autowiredConstructors.get(0);
            } else {
                return beanReference;
            }
        }

        beanConstructor = isNull(beanConstructor) ? declaredConstructors[0] : beanConstructor;
        var args = Arrays.stream(beanConstructor.getParameterTypes()).map(type -> context.getBean(type)).toArray();
        if (args.length != 0) {
            Object newBeanObject = beanConstructor.newInstance(args);

            beanReference.setBeanObject(newBeanObject);
        }

        return beanReference;
    }

    @Override
    public BeanReference postProcessAfterInitialization(BeanReference beanReference) {
        return beanReference;
    }

    @Override
    public int getOrder() {
        return 1;
    }
}

