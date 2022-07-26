package com.bobocode.petros.bring.context;

/**
 * Central interface to provide configuration for an application.
 */
public interface ApplicationContext {

    /**
     * Return an instance of the specified bean.
     *
     * @param requiredType type the bean must match; can be an interface or superclass
     * @return an instance of the bean
     */
    <T> T getBean(final Class<T> requiredType);

    /**
     * Return an instance of the specified bean.
     *
     * @param name         the name of the bean to retrieve
     * @param requiredType type the bean must match; can be an interface or superclass
     * @return an instance of the bean
     */
    <T> T getBean(final String name, final Class<T> requiredType);

}
