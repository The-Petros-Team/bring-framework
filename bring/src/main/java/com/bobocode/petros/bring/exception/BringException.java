package com.bobocode.petros.bring.exception;

/**
 * General framework-level exception. In case new exception should be registered within the codebase it should
 * extend this exception.
 */
public class BringException extends RuntimeException {

    public BringException(final String message) {
        super(message);
    }

    public BringException(String message, Throwable cause) {
        super(message, cause);
    }
}
