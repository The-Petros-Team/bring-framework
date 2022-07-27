package com.bobocode.petros.bring.context.domain;


/**
 * The constants of current enumeration describe the various
 * of bean creation scope. That impact to method of creating beans.
 */
public enum BeanScope {

    SINGLETON,
    PROTOTYPE;

    /**
     * Returns a scope as a string.
     *
     * @param scope scope
     * @return scope as string
     */
    public static String getScopeAsString(final BeanScope scope) {
        return scope.name();
    }

    /**
     * Return enum of scope from string.
     *
     * @param scope scope
     * @return BeanScope from provided String
     */
    public static BeanScope getScopeFromString(final String scope) { return Enum.valueOf(BeanScope.class, scope); }
}
