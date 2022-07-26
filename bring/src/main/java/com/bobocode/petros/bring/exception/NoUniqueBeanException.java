package com.bobocode.petros.bring.exception;

public class NoUniqueBeanException extends BringException {
    public NoUniqueBeanException(final String message) {
        super(message);
    }

    public NoUniqueBeanException(final String message, final Throwable cause) {
        super(message, cause);
    }
}
