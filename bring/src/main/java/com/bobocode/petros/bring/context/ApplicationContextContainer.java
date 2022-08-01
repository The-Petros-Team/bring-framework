package com.bobocode.petros.bring.context;

import com.bobocode.petros.bring.context.aware.ApplicationContextAware;
import com.bobocode.petros.bring.context.aware.injector.ApplicationContextInjector;
import com.bobocode.petros.bring.context.aware.injector.Injector;
import com.bobocode.petros.bring.context.domain.BeanReference;
import com.bobocode.petros.bring.factory.DefaultBeanFactory;
import com.bobocode.petros.bring.factory.postprocessor.DefaultBeanPostProcessorContainer;
import com.bobocode.petros.bring.registry.DefaultBeanDefinitionRegistry;
import com.bobocode.petros.bring.scanner.impl.DefaultClassPathBeanDefinitionScanner;
import com.bobocode.petros.bring.scanner.impl.DefaultConfigurationBeanDefinitionScanner;
import com.bobocode.petros.bring.utils.ScanningUtils;
import lombok.experimental.UtilityClass;

import java.util.Map;

@UtilityClass
public class ApplicationContextContainer {

    private static final String BEAN_POST_PROCESSORS_PACKAGE = "com.bobocode.petros.bring.factory.postprocessor";

    public ApplicationContext create(final String packageName) {
        var allClasses = ScanningUtils.getClassesFromPackages(packageName, BEAN_POST_PROCESSORS_PACKAGE);
        var context = new AnnotationConfigApplicationContext();

        final Injector<ApplicationContext> applicationContextInjector = ApplicationContextInjector.getInstance();
        applicationContextInjector.setInjectableType(context);

        var configurationScanner = new DefaultConfigurationBeanDefinitionScanner();
        var componentScanner = new DefaultClassPathBeanDefinitionScanner();
        context.setConfigurationBeanDefinitionScanner(configurationScanner);
        context.setComponentsBeanDefinitionScanner(componentScanner);
        context.registerBeanDefinitions(allClasses);

        final DefaultBeanPostProcessorContainer beanPostProcessorContainer = new DefaultBeanPostProcessorContainer(allClasses);
        var beanFactory = new DefaultBeanFactory(DefaultBeanDefinitionRegistry.getInstance());
        var beanReferences = beanFactory.getAllBeanReferences();

        // put to context
        final Map<String, BeanReference> beanNameToBeanReferenceMap = context.beanNameToBeanReferenceMap;
        beanNameToBeanReferenceMap.putAll(beanReferences);
        beanNameToBeanReferenceMap.values().forEach(beanReference -> {
            final Object beanObject = beanReference.getBeanObject();
            if (ScanningUtils.isAwareClass(beanObject, ApplicationContextAware.class)) {
                applicationContextInjector.inject(beanObject);
            }
            beanPostProcessorContainer.process(beanReference);
        });

        return context;
    }
}
