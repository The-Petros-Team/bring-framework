package com.bobocode.petros.bring.factory.postprocessor;

import com.bobocode.petros.bring.context.ApplicationContext;
import com.bobocode.petros.bring.context.domain.BeanReference;
import com.bobocode.petros.bring.context.domain.BeanScope;
import com.bobocode.petros.bring.exception.NoSuchBeanException;
import com.bobocode.petros.bring.exception.NoUniqueBeanException;
import com.bobocode.petros.bring.factory.postprocessor.mocks.fieldinjection.Car;
import com.bobocode.petros.bring.factory.postprocessor.mocks.fieldinjection.Engine;
import com.bobocode.petros.bring.factory.postprocessor.mocks.fieldinjection.Plane;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FieldInjectionBeanPostProcessorTest {
    @Mock
    private ApplicationContext applicationContext;

    private final Engine engine = new Engine();
    private final Car car = new Car();
    @InjectMocks
    private final FieldInjectionBeanPostProcessor fieldInjectionBeanPostProcessor = new FieldInjectionBeanPostProcessor();

    @Test
    void whenAutowiredFieldIsPresentAndBeanFoundByTypeThenInjectByType() {
        when(applicationContext.getBean(Engine.class)).thenReturn(engine);
        BeanReference beanReference = new BeanReference(car, BeanScope.SINGLETON);

        fieldInjectionBeanPostProcessor.postProcessBeforeInitialization(beanReference);

        assertEquals(engine, car.getEngine());
    }

    @Test
    void whenAutowiredFieldIsPresentAndNoUniqueBeanFoundByTypeThenInjectByNameAndType() {
        when(applicationContext.getBean(Engine.class)).thenThrow(NoUniqueBeanException.class);
        when(applicationContext.getBean("engine", Engine.class)).thenReturn(engine);
        BeanReference beanReference = new BeanReference(car, BeanScope.SINGLETON);

        fieldInjectionBeanPostProcessor.postProcessBeforeInitialization(beanReference);

        assertEquals(engine, car.getEngine());
    }

    @Test
    void whenAutowiredFieldIsPresentAndBeanNotFoundThenThrowException() {
        when(applicationContext.getBean(Engine.class)).thenThrow(NoSuchBeanException.class);
        BeanReference beanReference = new BeanReference(car, BeanScope.SINGLETON);

        assertThrows(NoSuchBeanException.class, () -> fieldInjectionBeanPostProcessor.postProcessBeforeInitialization(beanReference));
    }

    @Test
    void whenAutowiredFieldsAreNotPresentThenDoNotInject() {
        var plane = new Plane();
        BeanReference beanReference = new BeanReference(plane, BeanScope.SINGLETON);

        fieldInjectionBeanPostProcessor.postProcessBeforeInitialization(beanReference);

        assertNull(plane.getEngine());
    }

}