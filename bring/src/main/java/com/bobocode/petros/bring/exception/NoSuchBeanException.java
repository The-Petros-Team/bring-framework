package com.bobocode.petros.bring.exception;

/**
 * This exception is supposed to be thrown in case bean is not registered in context/registry.
 */
public class NoSuchBeanException extends BringException {

    public NoSuchBeanException(String message) {
        super(message);
    }
}
