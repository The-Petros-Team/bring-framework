package com.bobocode.petros.bring.context;

import com.bobocode.petros.bring.scanner.ClassPathBeanDefinitionScanner;
import com.bobocode.petros.bring.scanner.ConfigurationBeanDefinitionScanner;
import com.bobocode.petros.bring.scanner.impl.DefaultClassPathBeanDefinitionScanner;
import com.bobocode.petros.bring.scanner.impl.DefaultConfigurationBeanDefinitionScanner;

public class AnnotationConfigApplicationContext implements ApplicationContext {

    private final ConfigurationBeanDefinitionScanner configurationBeanDefinitionScanner;
    private final ClassPathBeanDefinitionScanner classPathBeanDefinitionScanner;

    public AnnotationConfigApplicationContext(final String packageName) {
        this.configurationBeanDefinitionScanner = new DefaultConfigurationBeanDefinitionScanner();
        this.classPathBeanDefinitionScanner = new DefaultClassPathBeanDefinitionScanner();
        // TODO scan package via Reflections lib
    }

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
