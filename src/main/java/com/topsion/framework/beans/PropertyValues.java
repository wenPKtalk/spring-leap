package com.topsion.framework.beans;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class PropertyValues {
    private final List<PropertyValue> propertyValues;
    public PropertyValues() {
        this.propertyValues = new ArrayList<>(16);
    }

    public List<PropertyValue> getPropertyValues() {
        return propertyValues;
    }

    public int size() {
        return this.propertyValues.size();
    }

    public void addPropertyValue(PropertyValue propertyValue) {
        this.propertyValues.add(propertyValue);
    }

    public void addPropertyValue(String type, String name, Object propertyValue, boolean isRef) {
        this.addPropertyValue(new PropertyValue(type, name, propertyValue, isRef));
    }

    public void removePropertyValue(Object propertyValue) {
        this.propertyValues.remove(propertyValue);
    }
    public void removePropertyValue(String name) {
        this.propertyValues.remove(getPropertyValue(name));
    }

    public PropertyValue getPropertyValue(String name) {
        return this.propertyValues.stream()
                .filter(pv -> pv.getName().equals(name))
                .findFirst()
                .orElse(null);
    }

    public Object get(String name) {
        PropertyValue pv = getPropertyValue(name);
        return Optional.ofNullable(pv)
                .map(PropertyValue::getValue)
                .orElse(null);
    }

    public boolean contains(String name) {
        return getPropertyValue(name) != null;
    }

    public boolean isEmpty() {
        return this.propertyValues.isEmpty();
    }

    public void addAllPropertyValues(List<PropertyValue> pvs) {
        this.propertyValues.addAll(pvs);
    }
}
