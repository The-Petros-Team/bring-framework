package com.bobocode.petros.bring.exception;

public enum ExceptionMessage {

    ;

    public static final String BEAN_NAME_IS_NOT_UNIQUE = "Bean name '%s' is not unique!";
    public static final String NULL_BEAN_DEFINITION = "BeanDefinition must not be null!";
    public static final String NULL_OR_EMPTY_BEAN_NAME = "Bean name must not be null or empty!";
    public static final String NO_SUCH_BEAN = "Bean with name '%s' is not registered!";
}
