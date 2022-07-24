package com.bobocode.petros.bring.context;

import com.bobocode.petros.bring.config.ContextConfig;
import com.bobocode.petros.bring.context.domain.BeanReference;
import com.bobocode.petros.bring.exception.NoSuchBeanException;
import com.bobocode.petros.bring.exception.NoUniqueBeanException;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import static com.bobocode.petros.bring.exception.ExceptionMessage.*;
import static com.bobocode.petros.bring.utils.BeanUtils.validateBeanName;
import static java.util.stream.Collectors.toMap;

/**
 * Implementation of {@link ApplicationContext}.
 */
public class AnnotationConfigApplicationContext implements ApplicationContext {

    private final Map<String, BeanReference> beanMap = new ConcurrentHashMap<>();
    private final ContextConfig config;

    public AnnotationConfigApplicationContext(ContextConfig config) {
        // TODO: 24.07.2022
        this.config = config;
    }

    @Override
    public <T> T getBean(Class<T> requiredType) {
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
        validateBeanName(name);
        return beanMap.entrySet().stream()
                .filter(e -> name.equals(e.getKey()))
                .filter(e -> requiredType.isAssignableFrom(e.getValue().getBeanObject().getClass()))
                .findFirst()
                .map(beanReference -> requiredType.cast(beanReference.getValue().getBeanObject()))
                .orElseThrow(() -> new NoSuchBeanException(NO_SUCH_BEAN_BY_NAME_AND_TYPE.formatted(name, requiredType)));
    }

    @Override
    public boolean isSingleton(String name) {
        validateBeanName(name);
        return Optional.ofNullable(beanMap.get(name))
                .map(BeanReference::isSingleton)
                .orElseThrow(() -> new NoSuchBeanException(NO_SUCH_BEAN_BY_NAME.formatted(name)));
    }

    @Override
    public boolean containsBean(String name) {
        return beanMap.containsKey(name);
    }

    // TODO: 24.07.2022
    @Override
    public void scanPackages(String... packageNames) {
        config.addPackages(packageNames);
    }

    // TODO: 24.07.2022
    @Override
    public void register(Class<?> componentClass) {
        config.registerClass(componentClass);
    }
}
