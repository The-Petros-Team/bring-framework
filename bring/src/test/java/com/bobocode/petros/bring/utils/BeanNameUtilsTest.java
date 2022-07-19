package com.bobocode.petros.bring.utils;

import com.bobocode.petros.bring.annotation.Bean;
import com.bobocode.petros.bring.annotation.Component;
import com.bobocode.petros.bring.annotation.Repository;
import com.bobocode.petros.bring.annotation.Service;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.lang.reflect.Method;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class BeanNameUtilsTest {

    @ParameterizedTest
    @MethodSource("getClassBeans")
    void getBeanNameFromClass(Class<?> clazz, String expectedName) {
        var name = BeanNameUtils.getBeanName(clazz);
        assertEquals(expectedName, name);
    }

    @Test
    void getBeanNameFromClassNotAnnotatedAsComponentBeanShouldThrowException() {
        assertThrows(IllegalArgumentException.class, () -> BeanNameUtils.getBeanName(NotAnnotatedClassAsComponent.class));
    }

    @ParameterizedTest
    @MethodSource("getMethodBeans")
    void getBeanNameFromMethod(Method method, String expectedName) {
        var name = BeanNameUtils.getBeanName(method);
        assertEquals(expectedName, name);
    }

    private static List<Object> getClassBeans() {
        return List.of(
                new Object[]{ComponentBeanWithNameInAnnotation.class, "testComponentBean"},
                new Object[]{ServiceBeanWithoutNameInAnnotation.class, "serviceBeanWithoutNameInAnnotation"},
                new Object[]{RepositoryBeanWithoutNameInAnnotation.class, "repositoryBeanWithoutNameInAnnotation"}
        );
    }

    private static List<Object> getMethodBeans() throws NoSuchMethodException {
        return List.of(
                new Object[]{ConfigClassWithMethodBeans.class.getMethod("testBean"), "testBean"},
                new Object[]{ConfigClassWithMethodBeans.class.getMethod("beanWithSpecialName"), "specialBean"}
        );
    }

    @Component(name = "testComponentBean")
    private static class ComponentBeanWithNameInAnnotation {
    }

    @Service
    private static class ServiceBeanWithoutNameInAnnotation {
    }

    @Repository
    private static class RepositoryBeanWithoutNameInAnnotation {
    }

    private static class NotAnnotatedClassAsComponent {
    }

    private static class ConfigClassWithMethodBeans {

        @Bean
        public Object testBean() {
            return new Object();
        }

        @Bean(name = "specialBean")
        public Object beanWithSpecialName() {
            return new Object();
        }
    }
}