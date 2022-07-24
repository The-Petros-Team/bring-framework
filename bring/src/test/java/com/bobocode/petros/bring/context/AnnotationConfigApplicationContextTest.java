package com.bobocode.petros.bring.context;

import com.bobocode.petros.bring.config.ContextConfig;
import com.bobocode.petros.bring.context.domain.BeanReference;
import com.bobocode.petros.bring.context.domain.BeanScope;
import com.bobocode.petros.bring.context.mocks.AfternoonService;
import com.bobocode.petros.bring.context.mocks.EveningService;
import com.bobocode.petros.bring.context.mocks.GreetingService;
import com.bobocode.petros.bring.context.mocks.MorningService;
import com.bobocode.petros.bring.exception.NoSuchBeanException;
import com.bobocode.petros.bring.exception.NoUniqueBeanException;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class AnnotationConfigApplicationContextTest {

    private final AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(new ContextConfig());

    private Map<String, BeanReference> beanMap;

    @BeforeEach
    @SneakyThrows
    public void beforeAll() {
        beanMap = new ConcurrentHashMap<>();
        var field = context.getClass().getDeclaredField("beanMap");
        field.setAccessible(true);
        field.set(context, beanMap);
    }

    @Test
    void whenContextContainsBeanOfSpecifiedTypeShouldReturnBeanInstance() {
        beanMap.put("afternoonService", createBeanReference(AfternoonService.class, BeanScope.SINGLETON));
        beanMap.put("eveningService", createBeanReference(EveningService.class, BeanScope.SINGLETON));
        beanMap.put("morningService", createBeanReference(MorningService.class, BeanScope.SINGLETON));

        var bean = context.getBean(AfternoonService.class);

        assertNotNull(bean);
        assertEquals(bean.getClass(), AfternoonService.class);
    }

    @Test
    void whenContextDoesntContainBeanThenThrowException() {
        beanMap.put("afternoonService", createBeanReference(AfternoonService.class, BeanScope.SINGLETON));
        beanMap.put("eveningService", createBeanReference(EveningService.class, BeanScope.SINGLETON));

        assertThrows(NoSuchBeanException.class, () -> context.getBean(MorningService.class));
    }

    @Test
    void whenContextContainsMoreThanOneBeanByTypeThenThrowException() {
        beanMap.put("afternoonService", createBeanReference(AfternoonService.class, BeanScope.SINGLETON));
        beanMap.put("eveningService", createBeanReference(EveningService.class, BeanScope.SINGLETON));
        beanMap.put("morningService", createBeanReference(MorningService.class, BeanScope.SINGLETON));

        assertThrows(NoUniqueBeanException.class, () -> context.getBean(GreetingService.class));
    }

    @Test
    void whenContextContainsBeanOfSpecifiedNameAndTypeShouldReturnBeanInstance() {
        beanMap.put("afternoonService", createBeanReference(AfternoonService.class, BeanScope.SINGLETON));
        beanMap.put("eveningService", createBeanReference(EveningService.class, BeanScope.SINGLETON));
        beanMap.put("morningService", createBeanReference(MorningService.class, BeanScope.SINGLETON));

        var bean = context.getBean("eveningService", GreetingService.class);

        assertNotNull(bean);
        assertTrue(bean instanceof EveningService);
    }

    @Test
    void whenBeanIsSingletonCheckIsSingletonShouldReturnTrue() {
        beanMap.put("afternoonService", createBeanReference(AfternoonService.class, BeanScope.SINGLETON));
        beanMap.put("eveningService", createBeanReference(EveningService.class, BeanScope.SINGLETON));
        beanMap.put("morningService", createBeanReference(MorningService.class, BeanScope.SINGLETON));

        var isSingleton = context.isSingleton("eveningService");

        assertTrue(isSingleton);
    }

    @Test
    void whenBeanIsPrototypeCheckIsSingletonShouldReturnFalse() {
        beanMap.put("afternoonService", createBeanReference(AfternoonService.class, BeanScope.SINGLETON));
        beanMap.put("eveningService", createBeanReference(EveningService.class, BeanScope.PROTOTYPE));
        beanMap.put("morningService", createBeanReference(MorningService.class, BeanScope.SINGLETON));

        var isSingleton = context.isSingleton("eveningService");

        assertFalse(isSingleton);
    }

    @Test
    void whenContextContainsBeanShouldReturnTrue() {
        beanMap.put("afternoonService", createBeanReference(AfternoonService.class, BeanScope.SINGLETON));
        beanMap.put("eveningService", createBeanReference(EveningService.class, BeanScope.PROTOTYPE));
        beanMap.put("morningService", createBeanReference(MorningService.class, BeanScope.SINGLETON));

        var isContains = context.containsBean("eveningService");

        assertTrue(isContains);
    }

    @Test
    void whenContextDoesntContainsBeanShouldReturnFalse() {
        beanMap.put("afternoonService", createBeanReference(AfternoonService.class, BeanScope.SINGLETON));
        beanMap.put("morningService", createBeanReference(MorningService.class, BeanScope.SINGLETON));

        var isContains = context.containsBean("eveningService");

        assertFalse(isContains);
    }

    @SneakyThrows
    private BeanReference createBeanReference(Class<?> beanClass, BeanScope scope) {
        return new BeanReference(beanClass.getDeclaredConstructor().newInstance(), scope);
    }
}