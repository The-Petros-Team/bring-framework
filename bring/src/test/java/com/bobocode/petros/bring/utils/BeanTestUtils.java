package com.bobocode.petros.bring.utils;

import com.bobocode.petros.bring.context.domain.BeanDefinition;
import com.bobocode.petros.bring.context.enums.Scope;

public final class BeanTestUtils {

    private BeanTestUtils() {}

    public static BeanDefinition createBeanDefinition(final String beanName, final Object beanClass) {
        var beanDefinition = new BeanDefinition();
        beanDefinition.setBeanName(beanName);
        beanDefinition.setBeanClass(beanClass);
        beanDefinition.setScope(Scope.SINGLETON.name());
        beanDefinition.setRequiresAutowire(true);
        return beanDefinition;
    }
}
