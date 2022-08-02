package com.bobocode.petros.bring.context.aware.injector;

/**
 * Generic interface that can be implemented in case a specific {@link com.bobocode.petros.bring.context.aware.Aware}
 * class requires a specific object to be injected.
 *
 * @param <T> generic type
 */
public interface Injector<T> {

    /**
     * performs injection on a given object.
     *
     * @param targetClass target class
     * @return class with injected object instance
     */
    Object inject(final Object targetClass);

    /**
     * Sets an injectable type for a specific injector implementation.
     *
     * @param injectableType injectable type
     */
    void setInjectableType(T injectableType);

    /**
     * Returns an instance of injectable type.
     *
     * @return injectable typea
     */
    T getInjectableType();

}
