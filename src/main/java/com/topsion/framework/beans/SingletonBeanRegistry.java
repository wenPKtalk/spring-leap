package com.topsion.framework.beans;

public interface SingletonBeanRegistry {
    void registerSingleton(String beanName, Object obj);

    Object getSingletonBean(String beanName);

    boolean containsSingleton(String beanName);
    String[] getSingletonNames();
}
