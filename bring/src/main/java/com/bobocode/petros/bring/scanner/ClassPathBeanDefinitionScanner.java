package com.bobocode.petros.bring.scanner;

import com.bobocode.petros.bring.context.domain.BeanDefinition;

import java.util.List;
import java.util.Set;

public interface ClassPathBeanDefinitionScanner {

    List<BeanDefinition> scan(final Set<Class<?>> classes);

}
