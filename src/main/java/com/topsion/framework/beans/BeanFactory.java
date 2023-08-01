package com.topsion.framework.beans;

import com.topsion.framework.BeanDefinition;

public interface BeanFactory {
    Object getBean(String beanName) throws BeansException;

    /**
     *
     * @param beanDefinition
     * @param obj obj 为 Object 类，
     *            指代与 beanName 对应的 Bean 的信息。
     *            你可以看下修改后的 BeanFactory。
     */
    void registerBean(BeanDefinition beanDefinition, Object obj);
}
