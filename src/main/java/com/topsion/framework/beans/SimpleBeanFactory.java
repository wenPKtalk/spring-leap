package com.topsion.framework.beans;

import com.topsion.framework.BeanDefinition;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class SimpleBeanFactory extends DefaultSingletonBeanRegistry implements BeanFactory {
    private Map<String, BeanDefinition> beanDefinitions = new ConcurrentHashMap();

    @Override
    public Object getBean(String beanName) throws BeansException {
        //尝试获取bean实例
        Object sigleton = this.getSingletonBean(beanName);
        //如果还没创建则根据定义创建实例
        if (sigleton == null) {
            BeanDefinition beanDefinition = beanDefinitions.get(beanName);
            if (Objects.isNull(beanDefinition)) {
                throw new BeansException("Can't found bean " + beanName);
            }

            //获取bean的定义
            try {
                sigleton = Class.forName(beanDefinition.getClassName()).getDeclaredConstructor().newInstance();
            } catch (ClassNotFoundException | InvocationTargetException | InstantiationException |
                     IllegalAccessException | NoSuchMethodException e) {
                throw new RuntimeException(e);
            }
            //注册bean 实例
            this.registerSingleton(beanDefinition.getId(), sigleton);
        }
        return sigleton;
    }

    private Object createBean(BeanDefinition beanDefinition) {
        Class<?> clz;
        Object obj;
        Constructor<?> con;

        try {
            clz = Class.forName(beanDefinition.getClassName());
            //处理构造器参数
            ArgumentValues constructorArgumentValues = beanDefinition.getConstructorArgumentValues();
            if (!constructorArgumentValues.isEmpty()) {
                Class<?>[] paramTypes = new Class<?>[constructorArgumentValues.getArgumentCount()];
                Object[] paramValues = new Object[constructorArgumentValues.getArgumentCount()];
                for (int i = 0; i < constructorArgumentValues.getArgumentCount(); i++) {
                    ArgumentValue argumentValue = constructorArgumentValues.getIndexedArgumentValue(i);
                    if ("String".equals(argumentValue.getType()) || "java.lang.String".equals(argumentValue.getType())) {
                        paramTypes[i] = String.class;
                        paramValues[i] = argumentValue.getValue();
                    }
                    if ("Integer".equals(argumentValue.getType()) || "java.lang.Integer".equals(argumentValue.getType())) {
                        paramTypes[i] = Integer.class;
                        paramValues[i] = Integer.valueOf((String) argumentValue.getValue());
                    }
                    if ("int".equals(argumentValue.getType())) {
                        paramTypes[i] = int.class;
                        paramValues[i] = Integer.valueOf((String) argumentValue.getValue());
                    } else { //默认为string
                        paramTypes[i] = String.class;
                        paramValues[i] = argumentValue.getValue();
                    }
                }
                try {
                    //按照特定构造器创建实例
                    con = clz.getConstructor(paramTypes);
                    obj = con.newInstance(paramValues);
                } catch (NoSuchMethodException | InvocationTargetException | SecurityException |
                         InstantiationException | IllegalAccessException | IllegalArgumentException e) {
                    throw new RuntimeException(e);
                }
            } else {
                obj = clz.getConstructor().newInstance();
            }

        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException |
                 InvocationTargetException | NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
        //处理属性
        PropertyValues propertyValues = beanDefinition.getPropertyValues();
        if (!propertyValues.isEmpty()) {
            for (int i = 0; i < propertyValues.size(); i++) {
                //对每一个属性，分数据类型分别处理
                PropertyValue propertyValue = propertyValues.getPropertyValues().get(i);
                Object value = propertyValue.getValue();
                String name = propertyValue.getName();
                String type = propertyValue.getType();
                Class[] paramTypes = new Class[1];
                if ("String".equals(type) || "java.lang.String".equals(type)) {
                    paramTypes[0] = String.class;
                } else if ("Integer".equals(type) || "java.lang.Integer".equals(type)) {
                    paramTypes[0] = Integer.class;
                } else if ("int".equals(type)) {
                    paramTypes[0] = int.class;
                } else { // 默认为string
                    paramTypes[0] = String.class;
                }
                Object[] paramValues = new Object[1];
                paramValues[0] = value;

                //按照setXXX规范查找Setter方法，调用Setter设置属性
                String setterMethodName = getSetterMethodName(name);
                Method method = null;
                try {
                    clz.getMethod(setterMethodName, paramTypes);
                } catch (NoSuchMethodException e) {
                    throw new RuntimeException(e);
                }

                try {
                    method.invoke(obj, paramValues);
                } catch (IllegalAccessException | InvocationTargetException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        return obj;
    }

    private String getSetterMethodName(String name) {
        return "set" + name.substring(0, 1).toUpperCase() + name.substring(1);
    }

    public void registerBeanDefinition(BeanDefinition beanDefinition) {
        this.beanDefinitions.put(beanDefinition.getId(), beanDefinition);
    }

    public boolean containsBean(String beanName) {
        return containsSingleton(beanName);
    }

    @Override
    public void registerBean(BeanDefinition beanDefinition, Object obj) {
        this.registerSingleton(beanDefinition.getId(), obj);
    }
}
