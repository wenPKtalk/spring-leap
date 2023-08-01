package com.topsion.framework.beans;

import com.topsion.framework.BeanDefinition;

import java.lang.reflect.InvocationTargetException;
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
