package com.bobocode.petros.bring.scanner.impl;

import com.bobocode.petros.bring.context.domain.BeanDefinition;
import com.bobocode.petros.bring.scanner.ClassPathBeanDefinitionScanner;

import java.util.List;
import java.util.Set;

public class DefaultClassPathBeanDefinitionScanner implements ClassPathBeanDefinitionScanner {

    @Override
    public List<BeanDefinition> scan(Set<Class<?>> classes) {
        return null;
    }
}
