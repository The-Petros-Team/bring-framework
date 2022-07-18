package com.bobocode.petros.bring.context.domain;

import java.util.Objects;

public class BeanReference {
    private Object beanObject;
    private boolean singleton = true;
    private boolean prototype = false;

    public Object getBeanObject() {
        return beanObject;
    }

    public void setBeanObject(Object beanObject) {
        this.beanObject = beanObject;
    }

    public boolean isSingleton() {
        return singleton;
    }

    public void setSingleton(boolean singleton) {
        this.singleton = singleton;
    }

    public boolean isPrototype() {
        return prototype;
    }

    public void setPrototype(boolean prototype) {
        this.prototype = prototype;
    }

    public BeanReference() {
    }

    public BeanReference(Object beanObject, boolean singleton, boolean prototype) {
        this.beanObject = beanObject;
        this.singleton = singleton;
        this.prototype = prototype;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (object == null || getClass() != object.getClass()) {
            return false;
        }
        BeanReference that = (BeanReference) object;
        return singleton == that.singleton && prototype == that.prototype && beanObject.equals(that.beanObject);
    }

    @Override
    public int hashCode() {
        return Objects.hash(beanObject, singleton, prototype);
    }

    @Override
    public String toString() {
        return "BeanReference{" +
                "beanObject=" + beanObject +
                ", isSingleton=" + singleton +
                ", isPrototype=" + prototype +
                '}';
    }
}
