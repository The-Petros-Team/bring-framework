package com.bobocode.petros.bring.components;

import com.bobocode.petros.bring.context.ApplicationContext;
import com.bobocode.petros.bring.context.ApplicationContextContainer;
import org.junit.jupiter.api.Test;

import static com.bobocode.petros.bring.TestData.CONTEXT;
import static org.junit.jupiter.api.Assertions.*;

class ComponentAnnotationTest {

    @Test
    void getCarComponentBeanShouldReturnBeanWithInjectedFields() {
        var carBeanByClass = CONTEXT.getBean(Car.class);
        var carBeanByNameAndClass = CONTEXT.getBean("car", Car.class);

        assertSame(carBeanByClass, carBeanByNameAndClass);
        assertNotNull(carBeanByClass.getEngine());
        assertEquals(4, carBeanByClass.getSeatsNumber());
    }

    @Test
    void getPlaneComponentBeanShouldReturnBeanWithInjectedFields() {
        var planeBeanByClass = CONTEXT.getBean(Plane.class);
        var planeBeanByNameAndClass = CONTEXT.getBean("plane", Plane.class);

        assertSame(planeBeanByClass, planeBeanByNameAndClass);
        assertNotNull(planeBeanByClass.getEngine());
        assertEquals(40, planeBeanByClass.getSeatsNumber());
    }
}
