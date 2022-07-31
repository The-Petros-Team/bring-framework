package com.bobocode.petros.bring.configuration;

import com.bobocode.petros.bring.components.Car;
import com.bobocode.petros.bring.components.Engine;
import com.bobocode.petros.bring.components.Plane;
import org.junit.jupiter.api.Test;

import static com.bobocode.petros.bring.TestData.CONTEXT;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;

class BringConfigurationTest {

    @Test
    void getEngineBeanFromConfigurationClassShouldBeTheSameInCarAndPlaneBeans() {
        var engineBeanFromConfiguration = CONTEXT.getBean(Engine.class);
        var carEngine = CONTEXT.getBean(Car.class).getEngine();
        var planeEngine = CONTEXT.getBean(Plane.class).getEngine();

        assertNotNull(engineBeanFromConfiguration);
        assertSame(engineBeanFromConfiguration, carEngine);
        assertSame(engineBeanFromConfiguration, planeEngine);
    }

}
