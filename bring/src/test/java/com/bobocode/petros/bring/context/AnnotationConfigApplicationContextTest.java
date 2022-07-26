package com.bobocode.petros.bring.context;

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
import org.mockito.Spy;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static org.junit.jupiter.api.Assertions.*;

class AnnotationConfigApplicationContextTest {

    private final AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();

    @Spy
    private Map<String, BeanReference> beanNameToBeanReferenceMap = new ConcurrentHashMap<>();

    @BeforeEach
    @SneakyThrows
    public void beforeAll() {
        var field = context.getClass().getDeclaredField("beanNameToBeanReferenceMap");
        field.setAccessible(true);
        field.set(context, beanNameToBeanReferenceMap);
    }

    @Test
    void whenContextContainsBeanOfSpecifiedTypeShouldReturnBeanInstance() {
        beanNameToBeanReferenceMap.put("afternoonService", createBeanReference(AfternoonService.class, BeanScope.SINGLETON));
        beanNameToBeanReferenceMap.put("eveningService", createBeanReference(EveningService.class, BeanScope.SINGLETON));
        beanNameToBeanReferenceMap.put("morningService", createBeanReference(MorningService.class, BeanScope.SINGLETON));

        var bean = context.getBean(AfternoonService.class);

        assertNotNull(bean);
        assertEquals(bean.getClass(), AfternoonService.class);
    }

    @Test
    void whenContextDoesntContainBeanThenThrowException() {
        beanNameToBeanReferenceMap.put("afternoonService", createBeanReference(AfternoonService.class, BeanScope.SINGLETON));
        beanNameToBeanReferenceMap.put("eveningService", createBeanReference(EveningService.class, BeanScope.SINGLETON));

        assertThrows(NoSuchBeanException.class, () -> context.getBean(MorningService.class));
    }

    @Test
    void whenContextContainsMoreThanOneBeanByTypeThenThrowException() {
        beanNameToBeanReferenceMap.put("afternoonService", createBeanReference(AfternoonService.class, BeanScope.SINGLETON));
        beanNameToBeanReferenceMap.put("eveningService", createBeanReference(EveningService.class, BeanScope.SINGLETON));
        beanNameToBeanReferenceMap.put("morningService", createBeanReference(MorningService.class, BeanScope.SINGLETON));

        assertThrows(NoUniqueBeanException.class, () -> context.getBean(GreetingService.class));
    }

    @Test
    void whenContextContainsBeanOfSpecifiedNameAndTypeShouldReturnBeanInstance() {
        beanNameToBeanReferenceMap.put("afternoonService", createBeanReference(AfternoonService.class, BeanScope.SINGLETON));
        beanNameToBeanReferenceMap.put("eveningService", createBeanReference(EveningService.class, BeanScope.SINGLETON));
        beanNameToBeanReferenceMap.put("morningService", createBeanReference(MorningService.class, BeanScope.SINGLETON));

        var bean = context.getBean("eveningService", GreetingService.class);

        assertNotNull(bean);
        assertTrue(bean instanceof EveningService);
    }

    @SneakyThrows
    private BeanReference createBeanReference(Class<?> beanClass, BeanScope scope) {
        return new BeanReference(beanClass.getDeclaredConstructor().newInstance(), scope);
    }
}