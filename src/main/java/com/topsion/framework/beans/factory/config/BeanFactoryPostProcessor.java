package com.topsion.framework.beans.factory.config;


import com.topsion.framework.beans.BeansException;
import com.topsion.framework.beans.factory.BeanFactory;

public interface BeanFactoryPostProcessor {
	void postProcessBeanFactory(BeanFactory beanFactory) throws BeansException;
}
