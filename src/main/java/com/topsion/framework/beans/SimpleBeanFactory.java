package com.topsion.framework.beans;

import com.topsion.framework.BeanDefinition;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SimpleBeanFactory implements BeanFactory{
    private List<BeanDefinition> beanDefinitions = new ArrayList<>();
    private List<String> beanNames = new ArrayList<>();
    private Map<String, Object> singletons = new HashMap<>();
    @Override
    public Object getBean(String beanName) throws BeansException {
        //尝试获取bean实例
        Object sigleton = singletons.get(beanName);
        //如果还没创建则根据定义创建实例
        if (sigleton == null) {
            int i = beanNames.indexOf(beanName);
            if (i == -1) {
                throw new BeansException("Can't found bean "+ beanName);
            }

            //获取bean的定义
            BeanDefinition beanDefinition = beanDefinitions.get(i);
            try {
                sigleton = Class.forName(beanDefinition.getClassName()).getDeclaredConstructor().newInstance();
            } catch (ClassNotFoundException | InvocationTargetException | InstantiationException |
                     IllegalAccessException | NoSuchMethodException e) {
                throw new RuntimeException(e);
            }
            //注册bean 实例
            this.singletons.put(beanDefinition.getId(), sigleton);
        }
        return sigleton;
    }

    @Override
    public void registerBeanDefinition(BeanDefinition beanDefinition) {
        this.beanDefinitions.add(beanDefinition);
        this.beanNames.add(beanDefinition.getId());
    }
}
