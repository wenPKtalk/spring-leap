package com.topsion.framework.beans;

import com.topsion.framework.BeanDefinition;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class SimpleBeanFactory extends DefaultSingletonBeanRegistry implements BeanFactory {
    private Map<String, BeanDefinition> beanDefinitions = new ConcurrentHashMap();
    private Map<String, Object> earlySingletonObjects = new ConcurrentHashMap<>();

    @Override
    public Object getBean(String beanName) throws BeansException {
        //尝试获取bean实例
        Object singleton = this.getSingletonBean(beanName);
        //如果还没创建则根据定义创建实例
        if (singleton == null) {
            //如果没有实例先尝试从毛坯实例集合中获取
            singleton = this.earlySingletonObjects.get(beanName);
            if (singleton == null) {
                //获取bean的定义
                BeanDefinition beanDefinition = beanDefinitions.get(beanName);
                if (Objects.isNull(beanDefinition)) {
                    throw new BeansException("Can't found bean " + beanName);
                }

                singleton = createBean(beanDefinition);
                //注册bean 实例
                this.registerSingleton(beanDefinition.getId(), singleton);
                // 预留beanpostprocessor位置
                // step 1: postProcessBeforeInitialization
                // step 2: afterPropertiesSet
                // step 3: init-method
                // step 4: postProcessAfterInitialization
            }
        }
        return singleton;
    }

    private Object createBean(BeanDefinition beanDefinition) {
        Class<?> clz;
        //创建bean的毛坯实例
        Object obj = doCreateBean(beanDefinition);
        //存放到毛胚实例缓存中
        this.earlySingletonObjects.put(beanDefinition.getId(), obj);
        try {
            clz = Class.forName(beanDefinition.getClassName());
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        //处理属性
        handleProperties(beanDefinition, clz, obj);
        return obj;
    }

    //doCreateBean创建毛胚实例，仅仅调用构造方法，没有进行属性处理
    private static Object doCreateBean(BeanDefinition beanDefinition) {
        Constructor<?> con;
        Object obj;
        try {
            Class<?> clz = Class.forName(beanDefinition.getClassName());
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
        return obj;
    }


    private void handleProperties(BeanDefinition beanDefinition, Class<?> clz, Object obj) {
        PropertyValues propertyValues = beanDefinition.getPropertyValues();
        if (!propertyValues.isEmpty()) {
            for (int i = 0; i < propertyValues.size(); i++) {
                //对每一个属性，分数据类型分别处理
                PropertyValue propertyValue = propertyValues.getPropertyValues().get(i);
                Object value = propertyValue.getValue();
                String name = propertyValue.getName();
                String type = propertyValue.getType();
                boolean isRef = propertyValue.isRef();
                Class[] paramTypes = new Class[1];
                Object[] paramValues = new Object[1];
                if (!isRef) {
                    if ("String".equals(type) || "java.lang.String".equals(type)) {
                        paramTypes[0] = String.class;
                    } else if ("Integer".equals(type) || "java.lang.Integer".equals(type)) {
                        paramTypes[0] = Integer.class;
                    } else if ("int".equals(type)) {
                        paramTypes[0] = int.class;
                    } else { // 默认为string
                        paramTypes[0] = String.class;
                    }
                    paramValues[0] = value;
                } else {

                    try {
                        paramTypes[0] = Class.forName(type);
                    } catch (ClassNotFoundException e) {
                        throw new RuntimeException(e);
                    }

                    try {
                        paramValues[0] = getBean((String) value);
                    } catch (BeansException e) {
                        throw new RuntimeException(e);
                    }
                }

                //按照setXXX规范查找Setter方法，调用Setter设置属性
                String setterMethodName = getSetterMethodName(name);
                Method method = null;
                try {
                    method = clz.getMethod(setterMethodName, paramTypes);
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
    }

    public void refresh() {
        Set<Map.Entry<String, BeanDefinition>> entries = beanDefinitions.entrySet();
        for (Map.Entry<String, BeanDefinition> entry : entries) {
            try {
                getBean(entry.getKey());
            } catch (BeansException e) {
                throw new RuntimeException(e);
            }
        }

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
