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

    /**
     * Check is the bean scope is {@link com.bobocode.petros.bring.context.domain.BeanScope#SINGLETON}.
     *
     * @param name the name of the bean to query
     * @return whether this bean corresponds to a singleton instance
     */
    boolean isSingleton(final String name);

    /**
     * Check is the bean with the given name present in context cache.
     *
     * @param name the name of the bean to query
     * @return whether a bean with the given name is present
     */
    boolean containsBean(final String name);

    void scanPackages(final String... packageNames);

    void register(final Class<?> componentClass);

}
