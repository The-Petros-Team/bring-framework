package com.bobocode.petros.bring.context;

import com.bobocode.petros.bring.factory.BeanFactory;

public interface ApplicationContext extends BeanFactory {

    BeanFactory getBeanFactory();

    void scanPackage(final String packageName);

    void register(final Class<?> componentClass);

}
