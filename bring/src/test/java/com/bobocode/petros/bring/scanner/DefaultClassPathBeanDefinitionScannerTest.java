package com.bobocode.petros.bring.scanner;

import com.bobocode.petros.bring.context.domain.BeanDefinition;
import com.bobocode.petros.bring.scanner.impl.DefaultClassPathBeanDefinitionScanner;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.reflections.Reflections;
import org.reflections.scanners.Scanners;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class DefaultClassPathBeanDefinitionScannerTest {
    private static final String ROOT_PACKAGE = "com.bobocode.petros.bring.scanner.mocks.components";
    private static final String COMPONENTS_WITH_NAME_PACKAGE = ROOT_PACKAGE + ".withname";
    private static final String COMPONENTS_WITHOUT_NAME_PACKAGE = ROOT_PACKAGE + ".withoutname";

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
        List<BeanDefinition> bicycleRepository = beanDefinitions.stream().filter(beanDefinition -> beanDefinition.getBeanName().equals("bicycleRepository")).toList();
        List<BeanDefinition> carComponent = beanDefinitions.stream().filter(beanDefinition -> beanDefinition.getBeanName().equals("carComponent")).toList();
        List<BeanDefinition> motorcycleService = beanDefinitions.stream().filter(beanDefinition -> beanDefinition.getBeanName().equals("motorcycleService")).toList();
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
        List<BeanDefinition> repository = beanDefinitions.stream().filter(beanDefinition -> beanDefinition.getBeanName().equals("repository")).toList();
        List<BeanDefinition> component = beanDefinitions.stream().filter(beanDefinition -> beanDefinition.getBeanName().equals("component")).toList();
        List<BeanDefinition> service = beanDefinitions.stream().filter(beanDefinition -> beanDefinition.getBeanName().equals("service")).toList();
        assertAll(
                () -> assertEquals(1, repository.size()),
                () -> assertEquals(1, component.size()),
                () -> assertEquals(1, service.size())
        );
    }
}
