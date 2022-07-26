package com.bobocode.petros.bring.scanner;

import com.bobocode.petros.bring.context.domain.BeanDefinition;
import com.bobocode.petros.bring.exception.ExceptionMessage;
import com.bobocode.petros.bring.exception.InvalidModifierException;
import com.bobocode.petros.bring.scanner.impl.DefaultConfigurationBeanDefinitionScanner;
import com.bobocode.petros.bring.scanner.mocks.scenario.methodwithargs.inotherconfig.Delivery;
import com.bobocode.petros.bring.scanner.mocks.scenario.methodwithargs.inotherconfig.ITPersonService;
import com.bobocode.petros.bring.scanner.mocks.scenario.methodwithargs.inotherconfig.PersonService;
import com.bobocode.petros.bring.scanner.mocks.scenario.methodwithargs.inotherconfig.PizzaDelivery;
import com.bobocode.petros.bring.scanner.mocks.scenario.methodwithargs.inthesameclass.BusTicketService;
import com.bobocode.petros.bring.scanner.mocks.scenario.methodwithargs.inthesameclass.PayPalPaymentService;
import com.bobocode.petros.bring.scanner.mocks.scenario.methodwithargs.inthesameclass.PaymentService;
import com.bobocode.petros.bring.scanner.mocks.scenario.methodwithargs.inthesameclass.TicketService;
import com.bobocode.petros.bring.scanner.mocks.scenario.methodwithargs.repository.BurgerService;
import com.bobocode.petros.bring.scanner.mocks.scenario.methodwithargs.repository.CashRepository;
import com.bobocode.petros.bring.scanner.mocks.scenario.methodwithargs.repository.KFCCashRepository;
import com.bobocode.petros.bring.scanner.mocks.scenario.methodwithargs.service.AnalyzerService;
import com.bobocode.petros.bring.scanner.mocks.scenario.methodwithargs.service.PictureAnalyzerService;
import com.bobocode.petros.bring.scanner.mocks.scenario.methodwithargs.service.PictureService;
import com.bobocode.petros.bring.scanner.mocks.scenario.positive.CloudService;
import com.bobocode.petros.bring.scanner.mocks.scenario.positive.MessageService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.reflections.Reflections;
import org.reflections.scanners.Scanners;

import java.util.Iterator;
import java.util.List;
import java.util.Set;

import static com.bobocode.petros.bring.context.domain.BeanScope.SINGLETON;
import static com.bobocode.petros.bring.context.domain.BeanScope.getScopeAsString;
import static com.bobocode.petros.bring.exception.ExceptionMessage.METHOD_MUST_NOT_BE_PRIVATE;
import static java.util.Comparator.comparing;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class ConfigurationBeanDefinitionScannerTest {

    private static final String ROOT_PACKAGE = "com.bobocode.petros.bring.scanner.mocks.scenario";

    private Reflections reflections;
    private final ConfigurationBeanDefinitionScanner configScanner = new DefaultConfigurationBeanDefinitionScanner();

    @SuppressWarnings("unchecked")
    @Test
    public void whenScanPackageThenCreateBeanDefinitionsFromConfigurationClasses() {
        // given
        String positiveScenarioPackage = ROOT_PACKAGE + ".positive";
        this.reflections = new Reflections(positiveScenarioPackage, Scanners.SubTypes.filterResultsBy(s -> true));
        final Set<Class<?>> allClasses = this.reflections.getSubTypesOf(Object.class);

        // then
        final List<BeanDefinition> beanDefinitions = this.configScanner.scan(allClasses);
        beanDefinitions.sort(comparing(BeanDefinition::getBeanName));

        // assertions & verification
        assertNotNull(beanDefinitions);
        assertEquals(2, beanDefinitions.size());
        final Iterator<BeanDefinition> iterator = beanDefinitions.iterator();
        final BeanDefinition cloudServiceBeanDefinition = iterator.next();
        assertAll(
                () -> assertEquals("cloudService", cloudServiceBeanDefinition.getBeanName()),
                () -> assertTrue(cloudServiceBeanDefinition.isInterface()),
                () -> assertEquals(CloudService.class.getName(), ((Class<CloudService>) cloudServiceBeanDefinition.getBeanClass()).getName()),
                () -> assertEquals(getScopeAsString(SINGLETON), cloudServiceBeanDefinition.getScope()),
                () -> assertEquals(0, cloudServiceBeanDefinition.getDependencies().size()),
                () -> assertEquals(1, cloudServiceBeanDefinition.getImplementations().size())
        );
        final BeanDefinition messageServiceBeanDefinition = iterator.next();
        assertAll(
                () -> assertEquals("messageService", messageServiceBeanDefinition.getBeanName()),
                () -> assertTrue(messageServiceBeanDefinition.isInterface()),
                () -> assertEquals(MessageService.class.getName(), ((Class<MessageService>) messageServiceBeanDefinition.getBeanClass()).getName()),
                () -> assertEquals(getScopeAsString(SINGLETON), messageServiceBeanDefinition.getScope()),
                () -> assertEquals(0, messageServiceBeanDefinition.getDependencies().size()),
                () -> assertEquals(1, messageServiceBeanDefinition.getImplementations().size())
        );
    }

    @Test
    public void whenScanEmptyPackageThenReturnEmptyBeanDefinitionCollection() {
        // given
        String emptyScenarioPackage = ROOT_PACKAGE + ".empty";
        this.reflections = new Reflections(emptyScenarioPackage, Scanners.SubTypes.filterResultsBy(s -> true));
        final Set<Class<?>> allClasses = this.reflections.getSubTypesOf(Object.class);

        // then
        final List<BeanDefinition> beanDefinitions = this.configScanner.scan(allClasses);

        // assertions & verification
        assertNotNull(beanDefinitions);
        assertEquals(0, beanDefinitions.size());
    }

    @Test
    public void whenScanPackageThenThrowExceptionDueToPrivateMethodModifier() {
        // given
        String privateMethodScenarioPackage = ROOT_PACKAGE + ".privatemethod";
        this.reflections = new Reflections(privateMethodScenarioPackage, Scanners.SubTypes.filterResultsBy(s -> true));
        final Set<Class<?>> allClasses = this.reflections.getSubTypesOf(Object.class);

        // then
        final InvalidModifierException invalidModifierException = assertThrows(
                InvalidModifierException.class, () -> this.configScanner.scan(allClasses)
        );

        // assertions & verification
        assertEquals(invalidModifierException.getMessage(), METHOD_MUST_NOT_BE_PRIVATE);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void whenScanPackageThenCreateBeanDefinitionThatHasArgumentMarkedAsComponent() {
        // given
        String methodWithComponentArgScenarioPackage = ROOT_PACKAGE + ".methodwithargs.component";
        this.reflections = new Reflections(methodWithComponentArgScenarioPackage, Scanners.SubTypes.filterResultsBy(s -> true));
        final Set<Class<?>> allClasses = this.reflections.getSubTypesOf(Object.class);

        // then
        final List<BeanDefinition> beanDefinitions = this.configScanner.scan(allClasses);

        // assertions & verification
        assertNotNull(beanDefinitions);
        assertEquals(1, beanDefinitions.size());
        final BeanDefinition cloudServiceBeanDefinition = beanDefinitions.iterator().next();
        assertAll(
                () -> assertEquals("cloudService", cloudServiceBeanDefinition.getBeanName()),
                () -> assertTrue(cloudServiceBeanDefinition.isInterface()),
                () -> assertEquals(
                        com.bobocode.petros.bring.scanner.mocks.scenario.methodwithargs.component.CloudService.class.getName(),
                        ((Class<com.bobocode.petros.bring.scanner.mocks.scenario.methodwithargs.component.CloudService>) cloudServiceBeanDefinition.getBeanClass()).getName()
                ),
                () -> assertEquals(getScopeAsString(SINGLETON), cloudServiceBeanDefinition.getScope()),
                () -> assertEquals(1, cloudServiceBeanDefinition.getDependencies().size()),
                () -> assertEquals(1, cloudServiceBeanDefinition.getImplementations().size())
        );
    }

    @SuppressWarnings("unchecked")
    @Test
    public void whenScanPackageThenCreateBeanDefinitionThatHasArgumentMarkedAsService() {
        // given
        String methodWithServiceArgScenarioPackage = ROOT_PACKAGE + ".methodwithargs.service";
        this.reflections = new Reflections(methodWithServiceArgScenarioPackage, Scanners.SubTypes.filterResultsBy(s -> true));
        final Set<Class<?>> allClasses = this.reflections.getSubTypesOf(Object.class);

        // then
        final List<BeanDefinition> beanDefinitions = this.configScanner.scan(allClasses);

        // assertions & verification
        assertNotNull(beanDefinitions);
        assertEquals(1, beanDefinitions.size());
        final BeanDefinition pictureServiceBeanDefinition = beanDefinitions.iterator().next();
        assertAll(
                () -> assertEquals("pictureService", pictureServiceBeanDefinition.getBeanName()),
                () -> assertTrue(pictureServiceBeanDefinition.isInterface()),
                () -> assertEquals(
                        PictureService.class.getName(),
                        ((Class<PictureService>) pictureServiceBeanDefinition.getBeanClass()).getName()
                ),
                () -> assertEquals(getScopeAsString(SINGLETON), pictureServiceBeanDefinition.getScope()),
                () -> assertEquals(1, pictureServiceBeanDefinition.getImplementations().size()),
                () -> assertEquals(1, pictureServiceBeanDefinition.getDependencies().size()),
                () -> assertEquals(
                        PictureAnalyzerService.class.getName(),
                        ((Class<PictureAnalyzerService>) pictureServiceBeanDefinition.getDependencies().values().iterator().next().getImplementation()).getName()
                ),
                () -> assertEquals(
                        AnalyzerService.class.getName(),
                        ((Class<AnalyzerService>) pictureServiceBeanDefinition.getDependencies().values().iterator().next().getInterfaceClass()).getName()
                )
        );
    }

    @SuppressWarnings("unchecked")
    @Test
    public void whenScanPackageThenCreateBeanDefinitionThatHasArgumentMarkedAsRepository() {
        // given
        String methodWithRepositoryArgScenarioPackage = ROOT_PACKAGE + ".methodwithargs.repository";
        this.reflections = new Reflections(methodWithRepositoryArgScenarioPackage, Scanners.SubTypes.filterResultsBy(s -> true));
        final Set<Class<?>> allClasses = this.reflections.getSubTypesOf(Object.class);

        // then
        final List<BeanDefinition> beanDefinitions = this.configScanner.scan(allClasses);

        // assertions & verification
        assertNotNull(beanDefinitions);
        assertEquals(1, beanDefinitions.size());
        final BeanDefinition burgerServiceBeanDefinition = beanDefinitions.iterator().next();
        assertAll(
                () -> assertEquals("burgerService", burgerServiceBeanDefinition.getBeanName()),
                () -> assertTrue(burgerServiceBeanDefinition.isInterface()),
                () -> assertEquals(
                        BurgerService.class.getName(),
                        ((Class<BurgerService>) burgerServiceBeanDefinition.getBeanClass()).getName()
                ),
                () -> assertEquals(getScopeAsString(SINGLETON), burgerServiceBeanDefinition.getScope()),
                () -> assertEquals(1, burgerServiceBeanDefinition.getImplementations().size()),
                () -> assertEquals(1, burgerServiceBeanDefinition.getDependencies().size()),
                () -> assertEquals(
                        KFCCashRepository.class.getName(),
                        ((Class<KFCCashRepository>) burgerServiceBeanDefinition.getDependencies().values().iterator().next().getImplementation()).getName()
                ),
                () -> assertEquals(
                        CashRepository.class.getName(),
                        ((Class<CashRepository>) burgerServiceBeanDefinition.getDependencies().values().iterator().next().getInterfaceClass()).getName()
                )
        );
    }

    @SuppressWarnings("unchecked")
    @Test
    public void whenScanPackageThenCreateBeanDefinitionsThatAreInTheSameConfig() {
        // given
        String methodWithBeansInTheSameConfigScenarioPackage = ROOT_PACKAGE + ".methodwithargs.inthesameclass";
        this.reflections = new Reflections(methodWithBeansInTheSameConfigScenarioPackage, Scanners.SubTypes.filterResultsBy(s -> true));
        final Set<Class<?>> allClasses = this.reflections.getSubTypesOf(Object.class);

        // then
        final List<BeanDefinition> beanDefinitions = this.configScanner.scan(allClasses);
        beanDefinitions.sort(comparing(BeanDefinition::getBeanName));

        // assertions & verification
        assertNotNull(beanDefinitions);
        assertEquals(2, beanDefinitions.size());
        final Iterator<BeanDefinition> iterator = beanDefinitions.iterator();
        final BeanDefinition paymentServiceBeanDefinition = iterator.next();
        assertAll(
                () -> assertEquals("paymentService", paymentServiceBeanDefinition.getBeanName()),
                () -> assertTrue(paymentServiceBeanDefinition.isInterface()),
                () -> assertEquals(
                        PaymentService.class.getName(),
                        ((Class<PaymentService>) paymentServiceBeanDefinition.getBeanClass()).getName()
                ),
                () -> assertEquals(getScopeAsString(SINGLETON), paymentServiceBeanDefinition.getScope()),
                () -> assertEquals(0, paymentServiceBeanDefinition.getDependencies().size()),
                () -> assertEquals(1, paymentServiceBeanDefinition.getImplementations().size()),
                () -> assertEquals(
                        PayPalPaymentService.class.getName(),
                        ((Class<PayPalPaymentService>) paymentServiceBeanDefinition.getImplementations().values().iterator().next()).getName()
                )
        );
        final BeanDefinition ticketServiceBeanDefinition = iterator.next();
        assertAll(
                () -> assertEquals("ticketService", ticketServiceBeanDefinition.getBeanName()),
                () -> assertTrue(ticketServiceBeanDefinition.isInterface()),
                () -> assertEquals(
                        TicketService.class.getName(),
                        ((Class<TicketService>) ticketServiceBeanDefinition.getBeanClass()).getName()
                ),
                () -> assertEquals(getScopeAsString(SINGLETON), ticketServiceBeanDefinition.getScope()),
                () -> assertEquals(1, ticketServiceBeanDefinition.getDependencies().size()),
                () -> assertEquals(1, ticketServiceBeanDefinition.getImplementations().size()),
                () -> assertEquals(
                        BusTicketService.class.getName(),
                        ((Class<BusTicketService>) ticketServiceBeanDefinition.getImplementations().values().iterator().next()).getName()
                )
        );
    }

    @SuppressWarnings("unchecked")
    @Test
    public void whenScanPackageThenCreateBeanDefinitionsThatAreLocatedInDifferentConfigFiles() {
        // given
        String methodWithBeansInSeparateConfigFilesScenarioPackage = ROOT_PACKAGE + ".methodwithargs.inotherconfig";
        this.reflections = new Reflections(methodWithBeansInSeparateConfigFilesScenarioPackage, Scanners.SubTypes.filterResultsBy(s -> true));
        final Set<Class<?>> allClasses = this.reflections.getSubTypesOf(Object.class);

        // then
        final List<BeanDefinition> beanDefinitions = this.configScanner.scan(allClasses);
        beanDefinitions.sort(comparing(BeanDefinition::getBeanName));

        // assertions & verification
        assertNotNull(beanDefinitions);
        assertEquals(2, beanDefinitions.size());
        final Iterator<BeanDefinition> iterator = beanDefinitions.iterator();
        final BeanDefinition deliveryBeanDefinition = iterator.next();
        assertAll(
                () -> assertEquals("delivery", deliveryBeanDefinition.getBeanName()),
                () -> assertTrue(deliveryBeanDefinition.isInterface()),
                () -> assertEquals(
                        Delivery.class.getName(),
                        ((Class<Delivery>) deliveryBeanDefinition.getBeanClass()).getName()
                ),
                () -> assertEquals(getScopeAsString(SINGLETON), deliveryBeanDefinition.getScope()),
                () -> assertEquals(0, deliveryBeanDefinition.getDependencies().size()),
                () -> assertEquals(1, deliveryBeanDefinition.getImplementations().size()),
                () -> assertEquals(
                        PizzaDelivery.class.getName(),
                        ((Class<PizzaDelivery>) deliveryBeanDefinition.getImplementations().values().iterator().next()).getName()
                )
        );
        final BeanDefinition personServiceBeanDefinition = iterator.next();
        assertAll(
                () -> assertEquals("personService", personServiceBeanDefinition.getBeanName()),
                () -> assertTrue(personServiceBeanDefinition.isInterface()),
                () -> assertEquals(
                        PersonService.class.getName(),
                        ((Class<PersonService>) personServiceBeanDefinition.getBeanClass()).getName()
                ),
                () -> assertEquals(getScopeAsString(SINGLETON), personServiceBeanDefinition.getScope()),
                () -> assertEquals(1, personServiceBeanDefinition.getDependencies().size()),
                () -> assertEquals(1, personServiceBeanDefinition.getImplementations().size()),
                () -> assertEquals(
                        ITPersonService.class.getName(),
                        ((Class<ITPersonService>) personServiceBeanDefinition.getImplementations().values().iterator().next()).getName()
                )
        );
    }

    @Test
    public void whenScanPackageThenThrowExceptionDueToDependencyClassThatIsNotRegisteredAsBeanCandidate() {
        // given
        String methodWithNotRegisteredArgScenarioPackage = ROOT_PACKAGE + ".methodwithargs.notabeancandidate";
        this.reflections = new Reflections(methodWithNotRegisteredArgScenarioPackage, Scanners.SubTypes.filterResultsBy(s -> true));
        final Set<Class<?>> allClasses = this.reflections.getSubTypesOf(Object.class);

        // then
        final IllegalArgumentException illegalArgumentException = assertThrows(
                IllegalArgumentException.class, () -> this.configScanner.scan(allClasses)
        );

        // assertions & verification
        String clazz = "com.bobocode.petros.bring.scanner.mocks.scenario.methodwithargs.notabeancandidate.DefaultExceptionHandler";
        assertEquals(ExceptionMessage.CLASS_IS_NOT_REGISTERED_AS_BEAN_CANDIDATE.formatted(clazz), illegalArgumentException.getMessage());
    }
}
