package com.bobocode.petros.bring.scanner.impl;

import com.bobocode.petros.bring.context.domain.BeanDefinition;
import com.bobocode.petros.bring.context.domain.Dependency;
import com.bobocode.petros.bring.scanner.ClassPathBeanDefinitionScanner;
import com.bobocode.petros.bring.utils.BeanNameUtils;
import com.bobocode.petros.bring.utils.ScanningUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.*;

import static com.bobocode.petros.bring.context.domain.BeanScope.SINGLETON;
import static com.bobocode.petros.bring.context.domain.BeanScope.getScopeAsString;

/**
 * Implementation of {@link DefaultClassPathBeanDefinitionScanner}
 *
 * @author YOliinyk
 * @version 0.0.1
 */
public class DefaultClassPathBeanDefinitionScanner implements ClassPathBeanDefinitionScanner {

    /**
     * {@inheritDoc}
     *
     * @param classes classes to scan
     * @return collection of bean definitions
     */
    @Override
    public List<BeanDefinition> scan(final Set<Class<?>> classes) {
        if (classes.isEmpty()) {
            return Collections.emptyList();
        }
        var beanDefinitions = new ArrayList<BeanDefinition>();
        final Set<Class<?>> componentClasses = ScanningUtils.findComponents(classes);
        for (final Class<?> componentClass : componentClasses) {
            final Set<Type> interfaces = ScanningUtils.getInterfaces(componentClass);
            final Class<?> anInterface = getAssignableInterface(interfaces, componentClass).orElse(null);
            final String beanName = BeanNameUtils.getBeanName(componentClass);
            var beanDefinition = new BeanDefinition();
            beanDefinition.setBeanName(beanName);
            beanDefinition.setScope(getScopeAsString(SINGLETON));
            beanDefinition.setBeanClass(componentClass);
            if (anInterface != null) {
                beanDefinition.setInterface(true);
                beanDefinition.setBeanClass(anInterface);
                beanDefinition.getImplementations().put(beanName, componentClass);
            }
            final Field[] fields = componentClass.getDeclaredFields();
            for (final Field field : fields) {
                final Class<?> type = field.getType();
                var dependency = getDependency(type, classes);
                var dependencyName = BeanNameUtils.createBeanName(((Class<?>) dependency.getImplementation()).getSimpleName());
                beanDefinition.getDependencies().put(dependencyName, dependency);
            }
            beanDefinitions.add(beanDefinition);
        }
        return beanDefinitions;
    }

    /**
     * Returns configured dependency.
     *
     * @param type       type
     * @param allClasses all classes
     * @return instance of dependency
     */
    private Dependency getDependency(final Class<?> type, final Set<Class<?>> allClasses) {
        var dependency = new Dependency();
        if (type.isInterface()) {
            final List<Class<?>> implementations = ScanningUtils.findImplementations(allClasses, type);
            ScanningUtils.checkImplementations(implementations.size(), type.getName());
            dependency.setInterfaceClass(type);
            final Class<?> implementation = implementations.iterator().next();
            dependency.setImplementation(implementation);
        } else {
            final Set<Type> interfaces = ScanningUtils.getInterfaces(type);
            if (interfaces.size() >= 1) {
                dependency.setInterfaceClass(interfaces.iterator().next());
            }
            dependency.setImplementation(type);
        }
        final boolean registeredAsComponent =
                ScanningUtils.isRegisteredAsComponent((Class<?>) dependency.getImplementation())
                        || ScanningUtils.isRegisteredGlobally(allClasses, (Class<?>) dependency.getImplementation());
        ScanningUtils.checkBeanCandidate((Class<?>) dependency.getImplementation(), registeredAsComponent);
        return dependency;
    }

    /**
     * Returns an interface that is assignable from a given component class.
     *
     * @param interfaces     set of interfaces to scan
     * @param componentClass component class
     * @return wrapped assignable interface or empty wrapper otherwise
     */
    private Optional<? extends Class<?>> getAssignableInterface(final Set<Type> interfaces, final Class<?> componentClass) {
        return interfaces.stream()
                .map(type -> (Class<?>) type)
                .filter(interfaze -> interfaze.isAssignableFrom(componentClass))
                .findAny();
    }
}
