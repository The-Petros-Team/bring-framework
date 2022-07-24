package com.bobocode.petros.bring.exception;

public class NoSuchBeanException extends BringException {

    public NoSuchBeanException(final String message) {
        super(message);
    }

    public NoSuchBeanException(final String message, final Throwable cause) {
        super(message, cause);
    }
}
