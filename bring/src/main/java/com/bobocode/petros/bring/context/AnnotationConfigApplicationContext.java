package com.bobocode.petros.bring.context;

public class AnnotationConfigApplicationContext implements ApplicationContext {

    @Override
    public <T> T getBean(Class<T> requiredType) {
        return null;
    }

    @Override
    public <T> T getBean(String name, Class<T> requiredType) {
        return null;
    }

    @Override
    public boolean isSingleton(String name) {
        return false;
    }

    @Override
    public boolean containsBean(String name) {
        return false;
    }

    @Override
    public boolean containsBeanDefinition(String beanName) {
        return false;
    }

    @Override
    public String[] getBeanDefinitionNames() {
        return new String[0];
    }

    @Override
    public void scanPackage(String packageName) {

    }

    @Override
    public void register(Class<?> componentClass) {

    }
}
