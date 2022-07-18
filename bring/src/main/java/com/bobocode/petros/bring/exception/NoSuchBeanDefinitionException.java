package com.bobocode.petros.bring.exception;

/**
 * This exception is supposed to be thrown in case bean is not registered in context/registry.
 */
public class NoSuchBeanDefinitionException extends BringException {

    public NoSuchBeanDefinitionException(String message) {
        super(message);
    }
}
