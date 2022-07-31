package com.bobocode.petros.bring.context;

/**
 * Class that provide access to application context
 */
public class ContextAware {
    private static ApplicationContext context;

    private ContextAware() {
    }

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
