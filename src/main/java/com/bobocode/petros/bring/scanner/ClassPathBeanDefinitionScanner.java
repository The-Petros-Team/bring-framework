package com.bobocode.petros.bring.scanner;

import com.bobocode.petros.bring.factory.BeanDefinition;

import java.util.List;

public interface ClassPathBeanDefinitionScanner {

    List<BeanDefinition> scan(final String packageName);

}
