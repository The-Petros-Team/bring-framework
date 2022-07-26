package com.bobocode.petros.bring.context;

import com.bobocode.petros.bring.scanner.impl.DefaultConfigurationBeanDefinitionScanner;
import lombok.experimental.UtilityClass;

@UtilityClass
public class ApplicationContextContainer {

    public ApplicationContext create(final String packageName) {
        var context = new AnnotationConfigApplicationContext();
        var configurationScanner = new DefaultConfigurationBeanDefinitionScanner();
        context.setConfigurationBeanDefinitionScanner(configurationScanner);
        context.registerBeanDefinitions(packageName);
        return context;
    }
}
