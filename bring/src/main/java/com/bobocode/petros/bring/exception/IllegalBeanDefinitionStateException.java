package com.bobocode.petros.bring.exception;

/**
 * Indicates that something is wrong with a bean definition state.
 * If, for example, there are 0 or more than 1 implementation are found during the scanning phase
 * this exception is supposed to be thrown.
 */
public class IllegalBeanDefinitionStateException extends BringException {

    public IllegalBeanDefinitionStateException(String message) {
        super(message);
    }

    public IllegalBeanDefinitionStateException(String message, Throwable cause) {
        super(message, cause);
    }
}
