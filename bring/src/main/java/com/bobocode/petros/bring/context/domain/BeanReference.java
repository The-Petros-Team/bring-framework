package com.bobocode.petros.bring.context.domain;

import java.util.Objects;

public class BeanReference {
    private Object beanObject;
    private boolean isSingleton = true;
    private boolean isPrototype = false;

    public Object getBeanObject() {
        return beanObject;
    }

    public void setBeanObject(Object beanObject) {
        this.beanObject = beanObject;
    }

    public boolean isSingleton() {
        return isSingleton;
    }

    public void setSingleton(boolean singleton) {
        isSingleton = singleton;
    }

    public boolean isPrototype() {
        return isPrototype;
    }

    public void setPrototype(boolean prototype) {
        isPrototype = prototype;
    }

    public BeanReference() {
    }

    public BeanReference(Object beanObject, boolean isSingleton, boolean isPrototype) {
        this.beanObject = beanObject;
        this.isSingleton = isSingleton;
        this.isPrototype = isPrototype;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BeanReference that = (BeanReference) o;
        return isSingleton == that.isSingleton && isPrototype == that.isPrototype && beanObject.equals(that.beanObject);
    }

    @Override
    public int hashCode() {
        return Objects.hash(beanObject, isSingleton, isPrototype);
    }

    @Override
    public String toString() {
        return "BeanReference{" +
                "beanObject=" + beanObject +
                ", isSingleton=" + isSingleton +
                ", isPrototype=" + isPrototype +
                '}';
    }
}
