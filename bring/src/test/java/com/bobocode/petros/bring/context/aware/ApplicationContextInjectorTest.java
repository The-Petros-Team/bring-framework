package com.bobocode.petros.bring.context.aware;

import com.bobocode.petros.bring.context.AnnotationConfigApplicationContext;
import com.bobocode.petros.bring.context.ApplicationContext;
import com.bobocode.petros.bring.context.aware.injector.ApplicationContextInjector;
import com.bobocode.petros.bring.context.aware.injector.Injector;
import com.bobocode.petros.bring.context.aware.postprocessor.NoOpBeanPostProcessor;
import com.bobocode.petros.bring.context.aware.service.EveningService;
import com.bobocode.petros.bring.utils.ReflectionTestUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class ApplicationContextInjectorTest {

    private final Injector<ApplicationContext> injector = ApplicationContextInjector.getInstance();

    @AfterEach
    public void setup() throws NoSuchFieldException {
        ReflectionTestUtils.setField(injector.getClass().getDeclaredField("applicationContext"), injector, null);
    }

    @Test
    public void whenTryInjectToObjectInstanceThenContextInjectedSuccessfully() {
        // given
        var context = new AnnotationConfigApplicationContext();
        injector.setInjectableType(context);
        var noOpPostProcessor = new NoOpBeanPostProcessor();

        // then
        assertNull(noOpPostProcessor.getContext());
        noOpPostProcessor = (NoOpBeanPostProcessor) this.injector.inject(noOpPostProcessor);

        // assertions & verification
        assertNotNull(noOpPostProcessor.getContext());
        assertEquals(context, noOpPostProcessor.getContext());
    }

    @Test
    public void whenTryGetApplicationContextFromInjectorBeforeSettingItThenThrowException() {
        final NullPointerException nullPointerException = assertThrows(NullPointerException.class, this.injector::getInjectableType);

        // assertions & verification
        assertEquals("Application context must be set before using it!", nullPointerException.getMessage());
    }

    @Test
    public void whenTryGetApplicationContextFromInjectorAfterSettingItThenReturnContext() {
        // given
        var context = new AnnotationConfigApplicationContext();
        this.injector.setInjectableType(context);

        // then
        final ApplicationContext injectableType = this.injector.getInjectableType();

        // assertions & verification
        assertNotNull(injectableType);
        assertEquals(context, injectableType);
    }

    @Test
    public void whenTrySetNullApplicationContextThenThrowException() {
        final NullPointerException nullPointerException = assertThrows(NullPointerException.class, () -> this.injector.setInjectableType(null));

        // assertions & verification
        assertEquals("It's not allowed to set null as injectable type!", nullPointerException.getMessage());
    }

    @Test
    public void whenTryToInjectApplicationContextToClassThatHasNoSuchFieldThenThrowException() {
        // given
        var eveningService = new EveningService();
        var context = new AnnotationConfigApplicationContext();
        this.injector.setInjectableType(context);

        // then
        final IllegalStateException illegalStateException = assertThrows(IllegalStateException.class, () -> this.injector.inject(eveningService));

        // assertions & verification
        assertEquals(
                "Field of type 'com.bobocode.petros.bring.context.ApplicationContext' not found in class " +
                        "'com.bobocode.petros.bring.context.aware.service.EveningService'",
                illegalStateException.getMessage()
        );
    }
}
