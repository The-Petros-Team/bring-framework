package com.bobocode.petros.bring.exception;

/**
 * This exception is supposed to be thrown if private method in a configuration class is marked via @Bean annotation.
 */
public class InvalidModifierException extends BringException {

    public InvalidModifierException(String message) {
        super(message);
    }

    public InvalidModifierException(String message, Throwable cause) {
        super(message, cause);
    }
}
