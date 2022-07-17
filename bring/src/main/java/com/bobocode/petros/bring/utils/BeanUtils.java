package com.bobocode.petros.bring.utils;

import com.bobocode.petros.bring.exception.InvalidBeanNameException;

import static com.bobocode.petros.bring.exception.ExceptionMessage.NULL_OR_EMPTY_BEAN_NAME;

public final class BeanUtils {

    private BeanUtils() {}

    public static void validateBeanName(final String beanName) {
        if (beanName == null || beanName.isBlank()) {
            throw new InvalidBeanNameException(NULL_OR_EMPTY_BEAN_NAME);
        }
    }

}
