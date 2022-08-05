package com.bobocode.petros.bring.scanner;

import com.bobocode.petros.bring.context.domain.BeanDefinition;
import com.bobocode.petros.bring.context.domain.BeanScope;
import com.bobocode.petros.bring.context.domain.Dependency;
import com.bobocode.petros.bring.scanner.impl.DefaultClassPathBeanDefinitionScanner;
import com.bobocode.petros.bring.scanner.mocks.components.withinterface.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.reflections.Reflections;
import org.reflections.scanners.Scanners;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class DefaultClassPathBeanDefinitionScannerTest {

    private static final String ROOT_PACKAGE = "com.bobocode.petros.bring.scanner.mocks.components";
    private static final String COMPONENTS_WITH_NAME_PACKAGE = ROOT_PACKAGE + ".withname";
    private static final String COMPONENTS_WITHOUT_NAME_PACKAGE = ROOT_PACKAGE + ".withoutname";
    private static final String BICYCLE_REPOSITORY_BEAN_NAME = "bicycleRepository";
    private static final String BICYCLE_REPOSITORY_PROVIDED_BEAN_NAME = "repository";
    private static final String MOTORCYCLE_SERVICE_BEAN_NAME = "motorcycleService";
    private static final String MOTORCYCLE_SERVICE_PROVIDED_BEAN_NAME = "service";
    private static final String CAR_COMPONENT_BEAN_NAME = "carComponent";
    private static final String CAR_COMPONENT_PROVIDED_BEAN_NAME = "component";
    private static final String COMPONENTS_WITH_INTERFACE_PACKAGE = ROOT_PACKAGE + ".withinterface";

    private Reflections reflections;
    private final DefaultClassPathBeanDefinitionScanner componentScanner = new DefaultClassPathBeanDefinitionScanner();

    @Test
    public void whenScanEmptyPackageThenReturnEmptyBeanDefinitionCollection() {
        // given
        String emptyScenarioPackage = ROOT_PACKAGE + ".empty";
        this.reflections = new Reflections(emptyScenarioPackage, Scanners.SubTypes.filterResultsBy(s -> true));
        final Set<Class<?>> allClasses = this.reflections.getSubTypesOf(Object.class);

        // then
        final List<BeanDefinition> beanDefinitions = this.componentScanner.scan(allClasses);

        // assertions & verification
        assertNotNull(beanDefinitions);
        assertEquals(0, beanDefinitions.size());
    }

    @Test
    public void whenScanPackageThenCreateBeanDefinitionForAllComponentsTypeWithoutCustomNames() {
        // given
        this.reflections = new Reflections(COMPONENTS_WITHOUT_NAME_PACKAGE, Scanners.SubTypes.filterResultsBy(s -> true));
        final Set<Class<?>> allClasses = this.reflections.getSubTypesOf(Object.class);

        // then
        final List<BeanDefinition> beanDefinitions = this.componentScanner.scan(allClasses);

        // assertions & verification
        assertNotNull(beanDefinitions);
        assertEquals(3, beanDefinitions.size());
        List<BeanDefinition> bicycleRepository = beanDefinitions.stream()
                .filter(beanDefinition -> beanDefinition.getBeanName().equals(BICYCLE_REPOSITORY_BEAN_NAME))
                .toList();
        List<BeanDefinition> carComponent = beanDefinitions.stream()
                .filter(beanDefinition -> beanDefinition.getBeanName().equals(CAR_COMPONENT_BEAN_NAME))
                .toList();
        List<BeanDefinition> motorcycleService = beanDefinitions
                .stream().filter(beanDefinition -> beanDefinition.getBeanName().equals(MOTORCYCLE_SERVICE_BEAN_NAME))
                .toList();
        assertAll(
                () -> assertEquals(1, bicycleRepository.size()),
                () -> assertEquals(1, carComponent.size()),
                () -> assertEquals(1, motorcycleService.size())
        );
    }

    @Test
    public void whenScanPackageThenCreateBeanDefinitionForAllComponentsTypeWithCustomNames() {
        // given
        this.reflections = new Reflections(COMPONENTS_WITH_NAME_PACKAGE, Scanners.SubTypes.filterResultsBy(s -> true));
        final Set<Class<?>> allClasses = this.reflections.getSubTypesOf(Object.class);

        // then
        final List<BeanDefinition> beanDefinitions = this.componentScanner.scan(allClasses);

        // assertions & verification
        assertNotNull(beanDefinitions);
        assertEquals(3, beanDefinitions.size());
        List<BeanDefinition> repository = beanDefinitions.stream()
                .filter(beanDefinition -> beanDefinition.getBeanName().equals(BICYCLE_REPOSITORY_PROVIDED_BEAN_NAME))
                .toList();
        List<BeanDefinition> component = beanDefinitions.stream()
                .filter(beanDefinition -> beanDefinition.getBeanName().equals(CAR_COMPONENT_PROVIDED_BEAN_NAME))
                .toList();
        List<BeanDefinition> service = beanDefinitions.stream()
                .filter(beanDefinition -> beanDefinition.getBeanName().equals(MOTORCYCLE_SERVICE_PROVIDED_BEAN_NAME))
                .toList();
        assertAll(
                () -> assertEquals(1, repository.size()),
                () -> assertEquals(1, component.size()),
                () -> assertEquals(1, service.size())
        );
    }

    @Test
    public void whenScanPackageThenCreateBeanDefinitionWithResolvedDependencies() {
        // given
        this.reflections = new Reflections(COMPONENTS_WITH_INTERFACE_PACKAGE, Scanners.SubTypes.filterResultsBy(s -> true));
        final Set<Class<?>> allClasses = this.reflections.getSubTypesOf(Object.class);

        // then
        final List<BeanDefinition> beanDefinitions = this.componentScanner.scan(allClasses);
        beanDefinitions.sort(Comparator.comparing(BeanDefinition::getBeanName));

        // assertions & verification
        assertNotNull(beanDefinitions);
        assertEquals(3, beanDefinitions.size());
        final Iterator<BeanDefinition> iterator = beanDefinitions.iterator();
        final BeanDefinition paymentService = iterator.next();
        assertAll(
                () -> assertEquals("paymentService", paymentService.getBeanName()),
                () -> assertEquals(PaymentService.class, paymentService.getBeanClass()),
                () -> assertFalse(paymentService.isInterface()),
                () -> assertEquals(BeanScope.getScopeAsString(BeanScope.SINGLETON), paymentService.getScope())
        );

        final BeanDefinition pizzaDeliveryService = iterator.next();
        assertAll(
                () -> assertEquals("pizzaDeliveryService", pizzaDeliveryService.getBeanName()),
                () -> assertEquals(DeliveryService.class, pizzaDeliveryService.getBeanClass()),
                () -> assertTrue(pizzaDeliveryService.isInterface()),
                () -> assertEquals(BeanScope.getScopeAsString(BeanScope.SINGLETON), pizzaDeliveryService.getScope())
        );
        final Map<String, Object> implementations = pizzaDeliveryService.getImplementations();
        assertAll(
                () -> assertEquals(1, implementations.size()),
                () -> assertEquals(PizzaDeliveryService.class, implementations.values().iterator().next())
        );
        final Map<String, Dependency> deliveryServiceDependencies = pizzaDeliveryService.getDependencies();
        final Dependency dependency = deliveryServiceDependencies.values().iterator().next();
        assertAll(
                () -> assertEquals(2, deliveryServiceDependencies.size()),
                () -> assertEquals(UserService.class, dependency.getInterfaceClass()),
                () -> assertEquals(UserServiceImpl.class, dependency.getImplementation())
        );

        final BeanDefinition userService = iterator.next();
        assertAll(
                () -> assertEquals("userServiceImpl", userService.getBeanName()),
                () -> assertEquals(UserService.class, userService.getBeanClass()),
                () -> assertTrue(userService.isInterface()),
                () -> assertEquals(BeanScope.getScopeAsString(BeanScope.SINGLETON), userService.getScope())
        );
        final Map<String, Object> userServiceImplementations = userService.getImplementations();
        assertAll(
                () -> assertEquals(1, userServiceImplementations.size()),
                () -> assertEquals(UserServiceImpl.class, userServiceImplementations.values().iterator().next())
        );
    }
}
