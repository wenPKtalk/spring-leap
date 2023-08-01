package com.topsion.framework.beans;

import com.topsion.framework.BeanDefinition;

public interface BeanFactory {
    Object getBean(String beanName) throws BeansException;

    void registerBean(BeanDefinition beanDefinition, Object obj);
}
