package com.bobocode.petros.bring.factory;

import com.bobocode.petros.bring.context.domain.BeanDefinition;
import com.bobocode.petros.bring.context.domain.BeanReference;

import java.util.Map;

public interface BeanFactory {

    Map<String, BeanReference> getAllBeanReference();

    BeanReference createBeanReference(String beanName);

    BeanReference createBeanReference(Class<?> beanType);

    BeanReference createBeanReference(BeanDefinition beanDefinition);
}
