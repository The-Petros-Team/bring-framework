package com.bobocode.petros.bring.context;

import com.bobocode.petros.bring.context.domain.BeanDefinition;
import com.bobocode.petros.bring.context.domain.BeanReference;
import com.bobocode.petros.bring.exception.NoSuchBeanException;
import com.bobocode.petros.bring.exception.NoUniqueBeanException;
import com.bobocode.petros.bring.registry.DefaultBeanDefinitionRegistry;
import com.bobocode.petros.bring.scanner.ConfigurationBeanDefinitionScanner;
import com.bobocode.petros.bring.scanner.impl.DefaultClassPathBeanDefinitionScanner;
import lombok.AccessLevel;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Stream;

import static com.bobocode.petros.bring.exception.ExceptionMessage.*;
import static com.bobocode.petros.bring.utils.BeanUtils.validateBeanName;
import static java.util.stream.Collectors.toMap;

/**
 * Implementation of {@link ApplicationContext}.
 */
@Slf4j
public class AnnotationConfigApplicationContext implements ApplicationContext {

    final Map<String, BeanReference> beanNameToBeanReferenceMap;

    @Setter(AccessLevel.PACKAGE)
    private ConfigurationBeanDefinitionScanner configurationBeanDefinitionScanner;

    @Setter(AccessLevel.PACKAGE)
    private DefaultClassPathBeanDefinitionScanner componentsBeanDefinitionScanner;

    public AnnotationConfigApplicationContext() {
        this.beanNameToBeanReferenceMap = new ConcurrentHashMap<>();
    }

    @Override
    public <T> T getBean(Class<T> requiredType) {
        Objects.requireNonNull(requiredType, "Input bean class must not be null!");

        var beans = this.beanNameToBeanReferenceMap.entrySet().stream()
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

        return beanNameToBeanReferenceMap.entrySet().stream()
                .filter(e -> name.equals(e.getKey()))
                .filter(e -> requiredType.isAssignableFrom(e.getValue().getBeanObject().getClass()))
                .findFirst()
                .map(beanReference -> requiredType.cast(beanReference.getValue().getBeanObject()))
                .orElseThrow(() -> new NoSuchBeanException(NO_SUCH_BEAN_BY_NAME_AND_TYPE.formatted(name, requiredType)));
    }

    void registerBeanDefinitions(final Set<Class<?>> allClasses) {
        Objects.requireNonNull(this.configurationBeanDefinitionScanner, "Configuration scanner must be initialized!");
        Objects.requireNonNull(this.componentsBeanDefinitionScanner, "Component scanner must be initialized!");
        var registry = DefaultBeanDefinitionRegistry.getInstance();
        log.info("Start bean definition scanning...");
        final List<BeanDefinition> beanDefinitions = Stream.concat(
                        this.configurationBeanDefinitionScanner.scan(allClasses).stream(),
                        this.componentsBeanDefinitionScanner.scan(allClasses).stream()
                )
                .toList();
        log.info("Scanning is finished. Found {} bean definitions", beanDefinitions.size());
        beanDefinitions.forEach(beanDefinition -> registry.registerBeanDefinition(beanDefinition.getBeanName(), beanDefinition));
    }
}
