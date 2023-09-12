package com.topsion.framework.beans.factory.config;

/**
 * 类的名称上带有 Registry 字样，
 * 所以让人一眼就能知道这里面存储的就是 Bean
 */
public interface SingletonBeanRegistry {
    void registerSingleton(String beanName, Object obj);

    Object getSingletonBean(String beanName);

    boolean containsSingleton(String beanName);
    String[] getSingletonNames();

    void removeBeanDefinition(String name);
}
