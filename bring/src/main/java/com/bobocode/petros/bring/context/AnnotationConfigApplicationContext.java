package com.bobocode.petros.bring.context;

import com.bobocode.petros.bring.context.domain.BeanReference;
import com.bobocode.petros.bring.exception.NoSuchBeanException;
import com.bobocode.petros.bring.exception.NoUniqueBeanException;
import com.bobocode.petros.bring.registry.DefaultBeanDefinitionRegistry;
import com.bobocode.petros.bring.scanner.ConfigurationBeanDefinitionScanner;
import com.bobocode.petros.bring.scanner.impl.DefaultConfigurationBeanDefinitionScanner;
import org.reflections.Reflections;
import org.reflections.scanners.Scanners;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

import static com.bobocode.petros.bring.exception.ExceptionMessage.*;
import static com.bobocode.petros.bring.utils.BeanUtils.validateBeanName;
import static java.util.stream.Collectors.toMap;

/**
 * Implementation of {@link ApplicationContext}.
 */
public class AnnotationConfigApplicationContext implements ApplicationContext {

    private final Map<String, BeanReference> beanMap = new ConcurrentHashMap<>();
    private ConfigurationBeanDefinitionScanner configurationBeanDefinitionScanner;


    public AnnotationConfigApplicationContext(final String packageName) {
        if (packageName == null || packageName.isBlank()) {
            throw new IllegalArgumentException(String.format("Invalid package '%s'", packageName));
        }
        this.configurationBeanDefinitionScanner = new DefaultConfigurationBeanDefinitionScanner();
        var reflections = new Reflections(packageName, Scanners.SubTypes.filterResultsBy(s -> true));
        var allClasses = reflections.getSubTypesOf(Object.class);
        var configurationBeanDefinitions = this.configurationBeanDefinitionScanner.scan(allClasses);

        var registry = DefaultBeanDefinitionRegistry.getInstance();
        configurationBeanDefinitions.forEach(
                beanDefinition -> registry.registerBeanDefinition(beanDefinition.getBeanName(), beanDefinition)
        );
    }

    @Override
    public <T> T getBean(Class<T> requiredType) {
        Objects.requireNonNull(requiredType, "Input bean class must not be null!");

        var beans = beanMap.entrySet().stream()
                .filter(e -> requiredType.isAssignableFrom(e.getValue().getBeanObject().getClass()))
                .collect(toMap(Map.Entry::getKey, Map.Entry::getValue));

        validateFoundBeans(beans, requiredType);

        return requiredType.cast(beans.values().iterator().next().getBeanObject());
    }

    private void validateFoundBeans(Map<?, ?> beans, Class<?> beanType) {
        if (beans.isEmpty()) {
            throw new NoSuchBeanException(NO_SUCH_BEAN_BY_TYPE.formatted(beanType.getName()));
        }
        if (beans.size() > 1) {
            throw new NoUniqueBeanException(NO_UNIQUE_BEAN.formatted(beanType.getName(), beans.keySet()));
        }
    }

    @Override
    public <T> T getBean(String name, Class<T> requiredType) {
        Objects.requireNonNull(requiredType, "Input bean class must not be null!");
        validateBeanName(name);

        return beanMap.entrySet().stream()
                .filter(e -> name.equals(e.getKey()))
                .filter(e -> requiredType.isAssignableFrom(e.getValue().getBeanObject().getClass()))
                .findFirst()
                .map(beanReference -> requiredType.cast(beanReference.getValue().getBeanObject()))
                .orElseThrow(() -> new NoSuchBeanException(NO_SUCH_BEAN_BY_NAME_AND_TYPE.formatted(name, requiredType)));
    }
}
