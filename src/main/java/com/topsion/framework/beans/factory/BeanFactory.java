package com.topsion.framework.beans.factory;

import com.topsion.framework.beans.factory.config.BeanDefinition;
import com.topsion.framework.beans.BeansException;

public interface BeanFactory {
    Object getBean(String beanName) throws BeansException;

    /**
     *
     * @param beanDefinition
     * @param obj obj 为 Object 类，
     *            指代与 beanName 对应的 Bean 的信息。
     *            你可以看下修改后的 BeanFactory。
     */
//    void registerBean(BeanDefinition beanDefinition, Object obj);

    /*boolean containsBean(String name);
    boolean isSingleton(String name);
    boolean isPrototype(String name);
    Class<?> getType(String name);*/

    boolean containsBean(String name);
    //void registerBean(String beanName, Object obj);
    boolean isSingleton(String name);
    boolean isPrototype(String name);
    Class<?> getType(String name);

}
