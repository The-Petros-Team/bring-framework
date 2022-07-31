package com.bobocode.petros.bring.context.utils;

import com.bobocode.petros.bring.context.ApplicationContext;
import lombok.experimental.UtilityClass;

/**
 * Class that provide access to application context
 */

@UtilityClass
public class ContextAwareUtils {
    private static ApplicationContext context;

    /**
     * Set application context
     * @param incomeContext - actual context
     */
    public static void setApplicationContext(ApplicationContext incomeContext) {
        context = incomeContext;
    }

    /**
     * Provide access to application context
     * @return actual application context
     */
    public static ApplicationContext getApplicationContext() {
        return context;
    }
}
