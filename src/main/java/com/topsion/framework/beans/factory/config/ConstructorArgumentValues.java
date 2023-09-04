package com.topsion.framework.beans.factory.config;

import java.util.*;

public class ConstructorArgumentValues {
    private final Map<Integer, ConstructorArgumentValue> indexArgumentValues = new HashMap<>(8);
    private final List<ConstructorArgumentValue> genericConstructorArgumentValues = new LinkedList<>();

    public ConstructorArgumentValues() {
    }

    public void addArgumentValue(Integer key, ConstructorArgumentValue constructorArgumentValue) {
        this.indexArgumentValues.put(key, constructorArgumentValue);
    }

    public boolean hasIndexArgumentValue(int index) {
        return this.indexArgumentValues.containsKey(index);
    }

    public void addGenericArgumentValue(Object value, String type) {
        this.addGenericArgumentValue(new ConstructorArgumentValue(value, type));
    }

    public void addGenericArgumentValue(ConstructorArgumentValue newConstructorArgumentValue) {
        if (newConstructorArgumentValue.getName() != null) {
            genericConstructorArgumentValues.removeIf(currentValue ->
                    newConstructorArgumentValue.getName().equals(currentValue.getName()));
        }

        this.genericConstructorArgumentValues.add(newConstructorArgumentValue);
    }

    public int getArgumentCount() {
        return this.genericConstructorArgumentValues.size();
    }

    public boolean isEmpty() {
        return this.genericConstructorArgumentValues.isEmpty();
    }

    public void addArgumentValues(List<ConstructorArgumentValue> arguments) {
        arguments.forEach(this::addGenericArgumentValue);
    }

    public ConstructorArgumentValue getIndexedArgumentValue(int index) {
        return this.genericConstructorArgumentValues.get(index);
    }
}
