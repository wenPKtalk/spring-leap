package com.topsion.framework.beans.factory.config;

import com.topsion.framework.beans.BeansException;
import com.topsion.framework.beans.factory.BeanFactory;

public interface BeanPostProcessor {
    /**
     * Bean初始化之前
     * @param bean
     * @param beanName
     * @return
     */
    Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException;

    /**
     * Bean 初始化之后
     * @param bean
     * @param beanName
     * @return
     */
    Object postProcessAfterInitialization(Object bean, String beanName);

    void setBeanFactory(BeanFactory beanFactory);


}
