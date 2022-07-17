package com.bobocode.petros.bring.exception;

/**
 * This exception is supposed to be thrown if something is wrong with a bean name.
 * For instance, bean name is null etc.
 */
public class InvalidBeanNameException extends BringException {

    public InvalidBeanNameException(final String message) {
        super(message);
    }
}
