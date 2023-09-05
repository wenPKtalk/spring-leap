package com.topsion.framework.beans.factory.annotation;

import com.topsion.framework.beans.BeansException;
import com.topsion.framework.beans.factory.BeanFactory;
import com.topsion.framework.beans.factory.config.BeanDefinition;

public class AutowiredCapableBeanFactory implements BeanFactory {
    @Override
    public Object getBean(String beanName) throws BeansException {
        return null;
    }

    @Override
    public void registerBean(BeanDefinition beanDefinition, Object obj) {

    }
}
