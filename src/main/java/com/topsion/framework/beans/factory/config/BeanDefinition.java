package com.topsion.framework.beans.factory.config;

import com.topsion.framework.beans.factory.config.ConstructorArgumentValues;
import com.topsion.framework.beans.PropertyValues;

/**
 * bean定义
 */
public class BeanDefinition {
    private String id;
    private String className;

    private PropertyValues propertyValues;
    private ConstructorArgumentValues constructorArgumentValues;
    private String[] dependsOnRef;

    public BeanDefinition(String id, String className) {
        this.id = id;
        this.className = className;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public void setPropertyValues(PropertyValues propertyValues) {
        this.propertyValues = propertyValues;
    }

    public void setConstructorArguments(ConstructorArgumentValues arguments) {
        this.constructorArgumentValues = arguments;
    }

    public PropertyValues getPropertyValues() {
        return propertyValues;
    }

    public ConstructorArgumentValues getConstructorArgumentValues() {
        return constructorArgumentValues;
    }

    public void setDependsOn(String[] refArray) {
        this.dependsOnRef = refArray;
    }

    public String getInitMethodName() {
        return null;
    }

    public boolean isPrototype() {
        return false;
    }

    public boolean isSingleton() {
        return false;
    }

    public boolean isLazyInit() {
        return false;
    }
}
