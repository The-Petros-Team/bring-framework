package com.bobocode.petros.bring.context.utils;

import com.bobocode.petros.bring.context.ApplicationContext;
import com.bobocode.petros.bring.context.ApplicationContextContainer;
import com.bobocode.petros.bring.context.utils.ContextAwareUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class ContextAwareUtilsTest {

    ApplicationContext context;

    @BeforeEach
    private void setup() {
        context = ApplicationContextContainer.create("com.bobocode.petros.bring.context");
    }

    @AfterEach
    private void cleanup() {
        ContextAwareUtils.setApplicationContext(null);
    }

    @Test
    void whenContextAwareSetApplicationContextThenSetAcceptedApplicationContext() {
        //given
        ContextAwareUtils.setApplicationContext(context);

        //then
        var contextFromContextAware = ContextAwareUtils.getApplicationContext();

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
        ContextAwareUtils.setApplicationContext(context);

        //then
        var contextFromContextAware = ContextAwareUtils.getApplicationContext();

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
        var contextFromContextAware = ContextAwareUtils.getApplicationContext();

        //assertions & verifications
        assertAll(
                () -> assertNull(contextFromContextAware),
                () -> assertNotEquals(contextFromContextAware, context),
                () -> assertNotNull(context)
        );
    }

    @Test
    void whenContextAwareSetApplicationContextNullThenContextAwareGetApplicationContextReturnNull() {
        //then
        var contextFromContextAware = ContextAwareUtils.getApplicationContext();

        //assertions & verifications
        assertAll(
                () -> assertNull(contextFromContextAware),
                () -> assertNotEquals(contextFromContextAware, context),
                () -> assertNotNull(context)
        );
    }
}
