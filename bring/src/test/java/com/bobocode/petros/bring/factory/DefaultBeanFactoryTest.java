package com.bobocode.petros.bring.factory;

import com.bobocode.petros.bring.context.domain.BeanDefinition;
import com.bobocode.petros.bring.context.domain.BeanReference;
import com.bobocode.petros.bring.context.domain.BeanScope;
import com.bobocode.petros.bring.exception.NoSuchBeanDefinitionException;
import com.bobocode.petros.bring.factory.mocks.EveningService;
import com.bobocode.petros.bring.factory.mocks.GreetingService;
import com.bobocode.petros.bring.factory.mocks.MorningService;
import com.bobocode.petros.bring.registry.BeanDefinitionRegistry;
import com.bobocode.petros.bring.registry.DefaultBeanDefinitionRegistry;
import com.bobocode.petros.bring.registry.utils.RegistryTestUtils;
import com.bobocode.petros.bring.utils.BeanTestUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.MockitoAnnotations.openMocks;

@ExtendWith(MockitoExtension.class)
class DefaultBeanFactoryTest {
    private BeanFactory factory;
    private BeanDefinitionRegistry registry;
    @Spy
    private Map<String, BeanDefinition> beanDefinitions = new ConcurrentHashMap<>();

    @BeforeEach
    public void setup() {
        openMocks(this);
        registry = DefaultBeanDefinitionRegistry.getInstance();
        RegistryTestUtils.configureRegistry(registry, beanDefinitions);
        factory = new DefaultBeanFactory(registry);
    }

    @AfterEach
    public void cleanup() {
        this.beanDefinitions.clear();
    }

    @Test
    void whenGetAllBeanReferenceInvokeWithNonEmptyBeanDefinitionRegistryThenReturnMap() {
        //given
        String firstBeanName = "morningService";
        BeanDefinition firstBeanDefinition = BeanTestUtils.createBeanDefinition(firstBeanName, MorningService.class);

        String secondBeanName = "eveningService";
        BeanDefinition secondBeanDefinition = BeanTestUtils.createBeanDefinition(secondBeanName, EveningService.class);

        registry.registerBeanDefinition(firstBeanName, firstBeanDefinition);
        registry.registerBeanDefinition(secondBeanName, secondBeanDefinition);

        //then
        Map<String, BeanReference> referenceMap = factory.getAllBeanReferences();

        // assertions & verification
        assertEquals(2, referenceMap.size());
        assertEquals(firstBeanDefinition.getBeanClass(), referenceMap.get(firstBeanName).getBeanObject().getClass());
        assertEquals(secondBeanDefinition.getBeanClass(), referenceMap.get(secondBeanName).getBeanObject().getClass());
        assertTrue(referenceMap.keySet()
                .stream()
                .allMatch(key -> key.contains(firstBeanName) || key.contains(secondBeanName)));
        assertTrue(referenceMap.keySet()
                .stream()
                .noneMatch(Objects::isNull));
        assertNotNull(referenceMap);
    }

    @Test
    void whenGetAllBeanReferenceInvokeWithEmptyBeanDefinitionRegistryThenReturnEmptyMap() {
        //then
        Map<String, BeanReference> referenceMap = factory.getAllBeanReferences();

        // assertions & verification
        assertTrue(referenceMap.isEmpty());
        assertNotNull(referenceMap);
    }

    @Test
    void whenCreateBeanReferenceInvokeThenReturnBeanReference() {
        //given
        BeanDefinition beanDefinition = BeanTestUtils.createBeanDefinition("morningService", MorningService.class);

        //then
        BeanReference beanReference = factory.createBeanReference(beanDefinition);

        // assertions & verifications
        assertNotNull(beanReference);
        assertEquals(beanReference.getBeanObject().getClass(), beanDefinition.getBeanClass());
        assertTrue(testScopeEquality(beanDefinition, beanReference));
    }

    @Test
    void whenCreateBeanReferenceInvokeWithInterfaceBeanDefinitionThenReturnBeanReference() {
        //given
        BeanDefinition beanDefinition = BeanTestUtils.createBeanDefinition("greetingService", GreetingService.class);
        beanDefinition.setInterface(true);
        beanDefinition.setImplementations(new HashMap<>() {{
            put("greetingService", MorningService.class);
        }});

        //then
        BeanReference beanReference = factory.createBeanReference(beanDefinition);

        // assertions & verifications
        assertNotNull(beanReference);
        assertTrue(beanDefinition.getImplementations().containsValue(beanReference.getBeanObject().getClass()));
        assertTrue(Arrays.stream(beanReference.getBeanObject().getClass().getInterfaces()).anyMatch(interfaceClass ->
                ((Class<?>) beanDefinition.getBeanClass()).isAssignableFrom(interfaceClass)));
        assertTrue(testScopeEquality(beanDefinition, beanReference));
    }

    @Test
    void whenCreateBeanReferenceInvokeWithInterfaceBeanDefinitionWithoutImplThenThrowException() {
        //given
        BeanDefinition beanDefinition = BeanTestUtils.createBeanDefinition("greetingService", GreetingService.class);
        beanDefinition.setInterface(true);

        // assertions & verifications
        assertThrows(NoSuchBeanDefinitionException.class, () -> factory.createBeanReference(beanDefinition));
    }

    @Test
    void whenCreateBeanReferenceInvokeWithAcceptNullBeanDefinitionThenThrowException() {
        //given
        BeanDefinition beanDefinition = null;

        //assertions & verifications
        assertThrows(NullPointerException.class, () -> factory.createBeanReference(beanDefinition));
    }

    private boolean testScopeEquality(BeanDefinition beanDefinition, BeanReference beanReference) {
        if (beanDefinition.getScope().equals(BeanScope.SINGLETON.name())) {
            return beanReference.isSingleton();
        } else {
            return beanReference.isPrototype();
        }
    }
}
