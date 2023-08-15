package com.topsion.framework;

import com.topsion.framework.beans.ArgumentValues;
import com.topsion.framework.beans.PropertyValues;

/**
 * bean定义
 */
public class BeanDefinition {
    private String id;
    private String className;

    private PropertyValues propertyValues;
    private ArgumentValues constructorArgumentValues;

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

    public void setConstructorArguments(ArgumentValues arguments) {
        this.constructorArgumentValues = arguments;
    }

    public PropertyValues getPropertyValues() {
        return propertyValues;
    }

    public ArgumentValues getConstructorArgumentValues() {
        return constructorArgumentValues;
    }
}
