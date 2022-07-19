package com.bobocode.petros.bring.exception;

/**
 * This exception is supposed to be thrown if provided bean name was registered in context/registry.
 */
public class NotUniqueBeanNameException extends BringException {

    public NotUniqueBeanNameException(final String message) {
        super(message);
    }

    public NotUniqueBeanNameException(String message, Throwable cause) {
        super(message, cause);
    }
}
