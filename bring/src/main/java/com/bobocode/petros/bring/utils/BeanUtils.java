package com.bobocode.petros.bring.utils;

import com.bobocode.petros.bring.exception.InvalidBeanNameException;
import lombok.experimental.UtilityClass;

import static com.bobocode.petros.bring.exception.ExceptionMessage.NULL_OR_EMPTY_BEAN_NAME;

/**
 * Utility class that provides different operations on beans/bean definitions.
 */
@UtilityClass
public final class BeanUtils {
    /**
     * Checks whether beanName is null, empty or blank and fails with an exception if at least one condition is true.
     *
     * @param beanName bean name to validate
     */
    public static void validateBeanName(final String beanName) {
        if (beanName == null || beanName.isBlank()) {
            throw new InvalidBeanNameException(NULL_OR_EMPTY_BEAN_NAME);
        }
    }

}
