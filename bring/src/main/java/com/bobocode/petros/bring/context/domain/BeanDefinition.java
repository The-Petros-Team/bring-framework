package com.bobocode.petros.bring.context.domain;

import java.util.Objects;

public class BeanDefinition {

    private String beanName;
    private String scope;
    private Object beanClass;
    private boolean requiresAutowire;

    public BeanDefinition() {
    }

    public String getBeanName() {
        return beanName;
    }

    public void setBeanName(String beanName) {
        this.beanName = beanName;
    }

    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }

    public Object getBeanClass() {
        return beanClass;
    }

    public void setBeanClass(Object beanClass) {
        this.beanClass = beanClass;
    }

    public boolean isRequiresAutowire() {
        return requiresAutowire;
    }

    public void setRequiresAutowire(boolean requiresAutowire) {
        this.requiresAutowire = requiresAutowire;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (object == null || getClass() != object.getClass()) {
            return false;
        }
        BeanDefinition that = (BeanDefinition) object;
        return requiresAutowire == that.requiresAutowire && Objects.equals(beanName, that.beanName) && Objects.equals(scope, that.scope) && Objects.equals(beanClass, that.beanClass);
    }

    @Override
    public int hashCode() {
        return Objects.hash(beanName, scope, beanClass, requiresAutowire);
    }

    @Override
    public String toString() {
        return "BeanDefinition{" +
                "beanName='" + beanName + '\'' +
                ", scope='" + scope + '\'' +
                ", beanClass=" + beanClass +
                ", requiresAutowire=" + requiresAutowire +
                '}';
    }
}
