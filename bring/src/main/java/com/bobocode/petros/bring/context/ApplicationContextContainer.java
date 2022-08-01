package com.bobocode.petros.bring.context;

import com.bobocode.petros.bring.context.domain.BeanReference;
import com.bobocode.petros.bring.factory.DefaultBeanFactory;
import com.bobocode.petros.bring.factory.postprocessor.BeanPostProcessor;
import com.bobocode.petros.bring.factory.postprocessor.DefaultBeanPostProcessorContainer;
import com.bobocode.petros.bring.registry.DefaultBeanDefinitionRegistry;
import com.bobocode.petros.bring.scanner.impl.DefaultBeanPostProcessorScanner;
import com.bobocode.petros.bring.scanner.impl.DefaultClassPathBeanDefinitionScanner;
import com.bobocode.petros.bring.scanner.impl.DefaultConfigurationBeanDefinitionScanner;
import com.bobocode.petros.bring.utils.ScanningUtils;
import lombok.experimental.UtilityClass;

import java.util.Map;

/**
 * This class serves as an entry point of Bring-based application.
 * It's required from a client to provide the package with configured classes, e.g.
 * marked with appropriate stereotype annotations like
 * {@link com.bobocode.petros.bring.annotation.Component}
 * {@link com.bobocode.petros.bring.annotation.Service}
 * {@link com.bobocode.petros.bring.annotation.Repository}
 * <p>
 * It's also possible to configure your classes via Java configuration using
 * {@link com.bobocode.petros.bring.annotation.Configuration}
 * {@link com.bobocode.petros.bring.annotation.Bean}
 * <p>
 * Provided package will be scanned in order to collect all pre-configured classes,
 * resolve their implementations and dependencies, instantiate them via {@link DefaultBeanFactory}
 * and configure them via built-in post-processors {@link BeanPostProcessor}.
 * As a result, {@link ApplicationContext} contains beans that are ready to use.
 * <p>
 * See also:
 * {@link AnnotationConfigApplicationContext}
 * {@link DefaultConfigurationBeanDefinitionScanner}
 * {@link DefaultClassPathBeanDefinitionScanner}
 * {@link DefaultBeanPostProcessorScanner}
 * {@link DefaultBeanPostProcessorContainer}
 * {@link DefaultBeanFactory}
 * {@link BeanPostProcessor}
 */
@UtilityClass
public class ApplicationContextContainer {

    private static final String BEAN_POST_PROCESSORS_PACKAGE = "com.bobocode.petros.bring.factory.postprocessor";

    /**
     * Entry point to Bring-based application.
     *
     * @param packageName package to scan
     * @return fully configured instance application context
     */
    public ApplicationContext create(final String packageName) {
        var allClasses = ScanningUtils.getClassesFromPackages(packageName, BEAN_POST_PROCESSORS_PACKAGE);
        var context = new AnnotationConfigApplicationContext();

        var configurationScanner = new DefaultConfigurationBeanDefinitionScanner();
        var componentScanner = new DefaultClassPathBeanDefinitionScanner();
        context.setConfigurationBeanDefinitionScanner(configurationScanner);
        context.setComponentsBeanDefinitionScanner(componentScanner);
        context.registerBeanDefinitions(allClasses);

        final DefaultBeanPostProcessorContainer beanPostProcessorContainer = new DefaultBeanPostProcessorContainer(allClasses);
        var beanFactory = new DefaultBeanFactory(DefaultBeanDefinitionRegistry.getInstance());
        var beanReferences = beanFactory.getAllBeanReferences();

        final Map<String, BeanReference> beanNameToBeanReferenceMap = context.beanNameToBeanReferenceMap;
        beanNameToBeanReferenceMap.putAll(beanReferences);
        beanNameToBeanReferenceMap.values().forEach(beanPostProcessorContainer::process);

        return context;
    }
}
