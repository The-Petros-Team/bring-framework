package com.bobocode.petros.bring.exception;

/**
 * Provides error messages in a single place.
 */
public interface ExceptionMessage {
    String BEAN_NAME_IS_NOT_UNIQUE = "Bean name '%s' is not unique!";
    String NULL_BEAN_DEFINITION = "BeanDefinition must not be null!";
    String NULL_OR_EMPTY_BEAN_NAME = "Bean name must not be null or empty!";
    String NO_SUCH_BEAN_DEFINITION = "Bean definition with name '%s' is not registered!";
    String CLASS_IS_NOT_REGISTERED_AS_BEAN_CANDIDATE = "Class '%s' is not registered as a bean candidate! Please make sure a bean is registered with @Component, @Service, @Repository annotations or in Java configuration.";
    String METHOD_MUST_NOT_BE_PRIVATE = "Method, annotated with @Bean annotation must not be private!";
    String INTERFACE_HAS_MORE_THEN_ONE_IMPLEMENTATION = "'%s' is an interface and must have only one implementation!";
    String NO_SUCH_BEAN_BY_TYPE = "Bean of type '%s' not found!";
    String NO_SUCH_BEAN_BY_NAME_AND_TYPE = "Bean with name '%s' of type %s is not found!";
    String NO_UNIQUE_BEAN = "More than one bean found for type %s: %s";
}
