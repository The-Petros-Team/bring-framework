package com.bobocode.petros.bring.context;

import com.bobocode.petros.bring.registry.DefaultBeanDefinitionRegistry;
import com.bobocode.petros.bring.scanner.ConfigurationBeanDefinitionScanner;
import com.bobocode.petros.bring.scanner.impl.DefaultConfigurationBeanDefinitionScanner;
import org.reflections.Reflections;
import org.reflections.scanners.SubTypesScanner;

public class AnnotationConfigApplicationContext implements ApplicationContext {

    private ConfigurationBeanDefinitionScanner configurationBeanDefinitionScanner;

    public AnnotationConfigApplicationContext(final String packageName) {
        if (packageName == null || packageName.isBlank()) {
            throw new IllegalArgumentException(String.format("Invalid package '%s'", packageName));
        }
        this.configurationBeanDefinitionScanner = new DefaultConfigurationBeanDefinitionScanner();
        var reflections = new Reflections(packageName, new SubTypesScanner(false));
        var allClasses = reflections.getSubTypesOf(Object.class);
        var configurationBeanDefinitions = this.configurationBeanDefinitionScanner.scan(allClasses);

        // register them via registry
        var registry = DefaultBeanDefinitionRegistry.getInstance();
        configurationBeanDefinitions.forEach(
                beanDefinition -> registry.registerBeanDefinition(beanDefinition.getBeanName(), beanDefinition)
        );
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
