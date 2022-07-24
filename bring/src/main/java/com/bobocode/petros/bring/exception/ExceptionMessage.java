package com.bobocode.petros.bring.exception;

/**
 * Provides error messages in a single place.
 */
public enum ExceptionMessage {

    ;

    public static final String BEAN_NAME_IS_NOT_UNIQUE = "Bean name '%s' is not unique!";
    public static final String NULL_BEAN_DEFINITION = "BeanDefinition must not be null!";
    public static final String NULL_OR_EMPTY_BEAN_NAME = "Bean name must not be null or empty!";
    public static final String NO_SUCH_BEAN_DEFINITION = "Bean definition with name '%s' is not registered!";
    public static final String CLASS_IS_NOT_REGISTERED_AS_BEAN_CANDIDATE = "Class '%s' is not registered as a bean candidate! Please make sure a bean is registered with @Component, @Service, @Repository annotations or in Java configuration.";
}
