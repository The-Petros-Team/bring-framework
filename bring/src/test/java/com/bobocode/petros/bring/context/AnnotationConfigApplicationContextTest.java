package com.bobocode.petros.bring.context;

import com.bobocode.petros.bring.context.domain.BeanDefinition;
import com.bobocode.petros.bring.context.domain.BeanReference;
import com.bobocode.petros.bring.context.domain.BeanScope;
import com.bobocode.petros.bring.context.mocks.AfternoonService;
import com.bobocode.petros.bring.context.mocks.EveningService;
import com.bobocode.petros.bring.context.mocks.GreetingService;
import com.bobocode.petros.bring.context.mocks.MorningService;
import com.bobocode.petros.bring.context.mocks.registerbeandefinitions.service.VisaPaymentService;
import com.bobocode.petros.bring.exception.NoSuchBeanException;
import com.bobocode.petros.bring.exception.NoUniqueBeanException;
import com.bobocode.petros.bring.registry.DefaultBeanDefinitionRegistry;
import com.bobocode.petros.bring.scanner.impl.DefaultClassPathBeanDefinitionScanner;
import com.bobocode.petros.bring.scanner.impl.DefaultConfigurationBeanDefinitionScanner;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Spy;
import org.reflections.Reflections;
import org.reflections.scanners.Scanners;

import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import static org.junit.jupiter.api.Assertions.*;

class AnnotationConfigApplicationContextTest {

    private static final String COMPONENTS_PACKAGE = "com.bobocode.petros.bring.context.mocks.registerbeandefinitions";

    private final AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();

    @Spy
    private Map<String, BeanReference> beanNameToBeanReferenceMap = new ConcurrentHashMap<>();

    @BeforeEach
    @SneakyThrows
    public void beforeAll() {
        var field = context.getClass().getDeclaredField("beanNameToBeanReferenceMap");
        field.setAccessible(true);
        field.set(context, beanNameToBeanReferenceMap);
    }

    @Test
    void whenContextContainsBeanOfSpecifiedTypeShouldReturnBeanInstance() {
        beanNameToBeanReferenceMap.put("afternoonService", createBeanReference(AfternoonService.class, BeanScope.SINGLETON));
        beanNameToBeanReferenceMap.put("eveningService", createBeanReference(EveningService.class, BeanScope.SINGLETON));
        beanNameToBeanReferenceMap.put("morningService", createBeanReference(MorningService.class, BeanScope.SINGLETON));

        var bean = context.getBean(AfternoonService.class);

        assertNotNull(bean);
        assertEquals(bean.getClass(), AfternoonService.class);
    }

    @Test
    void whenContextDoesntContainBeanThenThrowException() {
        beanNameToBeanReferenceMap.put("afternoonService", createBeanReference(AfternoonService.class, BeanScope.SINGLETON));
        beanNameToBeanReferenceMap.put("eveningService", createBeanReference(EveningService.class, BeanScope.SINGLETON));

        assertThrows(NoSuchBeanException.class, () -> context.getBean(MorningService.class));
    }

    @Test
    void whenContextContainsMoreThanOneBeanByTypeThenThrowException() {
        beanNameToBeanReferenceMap.put("afternoonService", createBeanReference(AfternoonService.class, BeanScope.SINGLETON));
        beanNameToBeanReferenceMap.put("eveningService", createBeanReference(EveningService.class, BeanScope.SINGLETON));
        beanNameToBeanReferenceMap.put("morningService", createBeanReference(MorningService.class, BeanScope.SINGLETON));

        assertThrows(NoUniqueBeanException.class, () -> context.getBean(GreetingService.class));
    }

    @Test
    void whenContextContainsBeanOfSpecifiedNameAndTypeShouldReturnBeanInstance() {
        beanNameToBeanReferenceMap.put("afternoonService", createBeanReference(AfternoonService.class, BeanScope.SINGLETON));
        beanNameToBeanReferenceMap.put("eveningService", createBeanReference(EveningService.class, BeanScope.SINGLETON));
        beanNameToBeanReferenceMap.put("morningService", createBeanReference(MorningService.class, BeanScope.SINGLETON));

        var bean = context.getBean("eveningService", GreetingService.class);

        assertNotNull(bean);
        assertTrue(bean instanceof EveningService);
    }

    @SneakyThrows
    private BeanReference createBeanReference(Class<?> beanClass, BeanScope scope) {
        return new BeanReference(beanClass.getDeclaredConstructor().newInstance(), scope);
    }

    @Test
    public void whenGetBeanByNameAndTypeThenThrowException() {
        // given
        beanNameToBeanReferenceMap.put("paymentService", createBeanReference(VisaPaymentService.class, BeanScope.SINGLETON));

        // then
        final NoSuchBeanException noSuchBeanException = assertThrows(NoSuchBeanException.class, () -> context.getBean("visaPaymentService", VisaPaymentService.class));

        // assertions & verification
        assertEquals(
                "Bean with name 'visaPaymentService' of type class com.bobocode.petros.bring.context.mocks.registerbeandefinitions.service.VisaPaymentService is not found!",
                noSuchBeanException.getMessage()
        );
    }

    @Test
    public void whenRunRegisterBeanDefinitionsThenBeanDefinitionsAreScannedAndStoredInRegistry() {
        // given
        var context = new AnnotationConfigApplicationContext();
        context.setConfigurationBeanDefinitionScanner(new DefaultConfigurationBeanDefinitionScanner());
        context.setComponentsBeanDefinitionScanner(new DefaultClassPathBeanDefinitionScanner());
        var reflections = new Reflections(COMPONENTS_PACKAGE, Scanners.SubTypes.filterResultsBy(s -> true));
        final Set<Class<?>> allClasses = reflections.getSubTypesOf(Object.class);

        // then
        context.registerBeanDefinitions(allClasses);

        // assertion & verification
        var registry = DefaultBeanDefinitionRegistry.getInstance();
        final Collection<BeanDefinition> beanDefinitions = registry.getAllBeanDefinitions();
        assertAll(
                () -> assertEquals(2, beanDefinitions.size()),
                () -> assertTrue(registry.containsBeanDefinition("paymentService")),
                () -> assertTrue(registry.containsBeanDefinition("userDetailsServiceImpl"))
        );
    }

    @Test
    public void whenRunRegisterBeanDefinitionsAndConfigScannerIsNotSetThenThrowException() {
        // given
        var context = new AnnotationConfigApplicationContext();
        context.setComponentsBeanDefinitionScanner(new DefaultClassPathBeanDefinitionScanner());
        var reflections = new Reflections(COMPONENTS_PACKAGE, Scanners.SubTypes.filterResultsBy(s -> true));
        final Set<Class<?>> allClasses = reflections.getSubTypesOf(Object.class);

        // then
        final NullPointerException nullPointerException = assertThrows(NullPointerException.class, () -> context.registerBeanDefinitions(allClasses));

        // assertions & verification
        assertEquals("Configuration scanner must be initialized!", nullPointerException.getMessage());
    }

    @Test
    public void whenRunRegisterBeanDefinitionsAndComponentScannerIsNotSetThenThrowException() {
        // given
        var context = new AnnotationConfigApplicationContext();
        context.setConfigurationBeanDefinitionScanner(new DefaultConfigurationBeanDefinitionScanner());
        var reflections = new Reflections(COMPONENTS_PACKAGE, Scanners.SubTypes.filterResultsBy(s -> true));
        final Set<Class<?>> allClasses = reflections.getSubTypesOf(Object.class);

        // then
        final NullPointerException nullPointerException = assertThrows(NullPointerException.class, () -> context.registerBeanDefinitions(allClasses));

        // assertions & verification
        assertEquals("Component scanner must be initialized!", nullPointerException.getMessage());
    }
}