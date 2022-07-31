package com.bobocode.petros.bring.context;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class ContextAwareTest {

    ApplicationContext context;

    @BeforeEach
    private void setup() {
        context = ApplicationContextContainer.create("com.bobocode.petros.bring.context");
    }

    @AfterEach
    private void cleanup() {
        ContextAware.setApplicationContext(null);
    }

    @Test
    void whenContextAwareSetApplicationContextThenSetAcceptedApplicationContext() {
        //given
        ContextAware.setApplicationContext(context);

        //then
        var contextFromContextAware = ContextAware.getApplicationContext();

        //assertions & verifications
        assertAll(
                () -> assertEquals(context, contextFromContextAware),
                () -> assertNotNull(contextFromContextAware),
                () -> assertNotNull(context)
        );
    }

    @Test
    void whenContextAwareGetApplicationContextThenReturnApplicationContext() {
        //given
        ContextAware.setApplicationContext(context);

        //then
        var contextFromContextAware = ContextAware.getApplicationContext();

        //assertions & verifications
        assertAll(
                () -> assertEquals(context, contextFromContextAware),
                () -> assertNotNull(contextFromContextAware),
                () -> assertNotNull(context)
        );
    }

    @Test
    void whenContextAwareGetApplicationContextWithoutSetContextBeforeThenReturnNull() {
        //then
        var contextFromContextAware = ContextAware.getApplicationContext();

        //assertions & verifications
        assertAll(
                () -> assertNull(contextFromContextAware),
                () -> assertNotEquals(context, contextFromContextAware),
                () -> assertNotNull(context)
        );
    }

    @Test
    void whenContextAwareSetApplicationContextNullThenContextAwareGetApplicationContextReturnNull() {
        //then
        var contextFromContextAware = ContextAware.getApplicationContext();

        //assertions & verifications
        assertAll(
                () -> assertNull(contextFromContextAware),
                () -> assertNotEquals(context, contextFromContextAware),
                () -> assertNotNull(context)
        );
    }
}
