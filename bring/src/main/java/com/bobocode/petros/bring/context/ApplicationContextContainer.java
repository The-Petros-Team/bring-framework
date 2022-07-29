package com.bobocode.petros.bring.context;

import com.bobocode.petros.bring.context.domain.BeanReference;
import com.bobocode.petros.bring.factory.DefaultBeanFactory;
import com.bobocode.petros.bring.factory.postprocessor.DefaultBeanPostProcessorContainer;
import com.bobocode.petros.bring.registry.DefaultBeanDefinitionRegistry;
import com.bobocode.petros.bring.scanner.impl.DefaultConfigurationBeanDefinitionScanner;
import com.bobocode.petros.bring.utils.ScanningUtils;
import lombok.experimental.UtilityClass;

import java.util.Map;

@UtilityClass
public class ApplicationContextContainer {

    public ApplicationContext create(final String packageName) {
        var allClasses = ScanningUtils.getClassesFromPackage(packageName);
        var context = new AnnotationConfigApplicationContext();

        var configurationScanner = new DefaultConfigurationBeanDefinitionScanner();
        context.setConfigurationBeanDefinitionScanner(configurationScanner);
        context.registerBeanDefinitions(allClasses);

        final DefaultBeanPostProcessorContainer beanPostProcessorContainer = new DefaultBeanPostProcessorContainer(allClasses);
        var beanFactory = new DefaultBeanFactory(
                DefaultBeanDefinitionRegistry.getInstance(), beanPostProcessorContainer
        );
        var beanReferences = beanFactory.getAllBeanReferences();

        // put to context
        final Map<String, BeanReference> beanNameToBeanReferenceMap = context.beanNameToBeanReferenceMap;
        beanNameToBeanReferenceMap.putAll(beanReferences);

        return context;
    }
}
