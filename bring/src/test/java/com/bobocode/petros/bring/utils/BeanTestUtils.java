package com.bobocode.petros.bring.utils;

import com.bobocode.petros.bring.context.domain.BeanDefinition;

import static com.bobocode.petros.bring.context.domain.BeanScope.SINGLETON;
import static com.bobocode.petros.bring.context.domain.BeanScope.getScopeAsString;

public final class BeanTestUtils {

    private BeanTestUtils() {}

    public static BeanDefinition createBeanDefinition(final String beanName, final Object beanClass) {
        var beanDefinition = new BeanDefinition();
        beanDefinition.setBeanName(beanName);
        beanDefinition.setBeanClass(beanClass);
        beanDefinition.setScope(getScopeAsString(SINGLETON));
        beanDefinition.setRequiresAutowire(true);
        return beanDefinition;
    }
}
