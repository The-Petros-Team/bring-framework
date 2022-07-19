package com.bobocode.petros.bring.context.domain;

import java.util.Objects;

/**
 * Class {@link BeanReference} store needed information to use beans in application
 * first to get information about bean scope and if
 * it`s Prototype create new bean and inject it instead of get bean created previously.
 */
public class BeanReference {
    private Object beanObject;
    private BeanScope beanScope = BeanScope.SINGLETON;

    public BeanReference() {
    }

    public BeanReference(Object beanObject, BeanScope beanScope) {
        this.beanObject = beanObject;
        this.beanScope = beanScope;
    }

    public Object getBeanObject() {
        return beanObject;
    }

    public void setBeanObject(Object beanObject) {
        this.beanObject = beanObject;
    }


    public boolean isSingleton() {
        return beanScope == BeanScope.SINGLETON;
    }

    public boolean isPrototype() {
        return beanScope == BeanScope.PROTOTYPE;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BeanReference that = (BeanReference) o;
        return Objects.equals(beanObject, that.beanObject) && beanScope == that.beanScope;
    }

    @Override
    public int hashCode() {
        return Objects.hash(beanObject, beanScope);
    }

    @Override
    public String toString() {
        return "BeanReference{" +
                "beanObject=" + beanObject +
                ", beanScope=" + beanScope +
                '}';
    }
}
