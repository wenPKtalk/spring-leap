package com.topsion.framework.beans;

import java.util.*;

public class ArgumentValues {
    private final Map<Integer, ArgumentValue> indexArgumentValues = new HashMap<>(8);
    private final List<ArgumentValue> genericArgumentValues = new LinkedList<>();

    public ArgumentValues() {
    }

    public void addArgumentValue(Integer key, ArgumentValue argumentValue) {
        this.indexArgumentValues.put(key, argumentValue);
    }

    public boolean hasIndexArgumentValue(int index) {
        return this.indexArgumentValues.containsKey(index);
    }

    public void addGenericArgumentValue(Object value, String type) {
        this.addGenericArgumentValue(new ArgumentValue(value, type));
    }

    public void addGenericArgumentValue(ArgumentValue newArgumentValue) {
        if (newArgumentValue.getName() != null) {
            genericArgumentValues.removeIf(currentValue ->
                    newArgumentValue.getName().equals(currentValue.getName()));
        }

        this.genericArgumentValues.add(newArgumentValue);
    }

    public int getArgumentCount() {
        return this.genericArgumentValues.size();
    }

    public boolean isEmpty() {
        return this.genericArgumentValues.isEmpty();
    }

    public void addArgumentValues(List<ArgumentValue> arguments) {
        arguments.forEach(this::addGenericArgumentValue);
    }

    public ArgumentValue getIndexedArgumentValue(int index) {
        return this.genericArgumentValues.get(index);
    }
}
