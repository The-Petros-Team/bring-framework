package com.bobocode.petros.bring.service;

import com.bobocode.petros.bring.exception.NoSuchBeanException;
import com.bobocode.petros.bring.exception.NoUniqueBeanException;
import com.bobocode.petros.bring.service.impl.MorningServiceImpl;
import org.junit.jupiter.api.Test;

import static com.bobocode.petros.bring.TestData.CONTEXT;
import static org.junit.jupiter.api.Assertions.*;

class ServiceTest {

    @Test
    void getMorningServiceShouldReturnBeanWithInjectedEveningService() {
        var morningService = CONTEXT.getBean(MorningServiceImpl.class);

        assertNotNull(morningService);
        assertNotNull(morningService.getEveningService());
        assertEquals("Good morning, Petros!", morningService.createGreeting("Petros"));
        assertEquals("Good evening, Petros!", morningService.getEveningService().createGreeting("Petros"));
    }

    @Test
    void getBeanByInterfaceWithMultipleImplementationShouldThrowNoUniqueBeanException() {
        assertThrows(NoUniqueBeanException.class, () -> CONTEXT.getBean(GreetingService.class));
    }

    @Test
    void getBeanByInterfaceWithMultipleImplementationAndBeanNameShouldReturnCorrectName() {
        var eveningBean = CONTEXT.getBean("evening", GreetingService.class);

        assertNotNull(eveningBean);
        assertEquals("Good evening, Buddy!", eveningBean.createGreeting("Buddy"));
    }

    @Test
    void getBeanByWrongBeanNameShouldThrowNoSuchBeanException() {
        assertThrows(NoSuchBeanException.class, () -> CONTEXT.getBean("morning", GreetingService.class));
    }
}
