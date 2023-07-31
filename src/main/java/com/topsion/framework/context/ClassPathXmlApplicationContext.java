package com.topsion.framework.context;

import com.topsion.framework.BeanDefinition;
import com.topsion.framework.beans.BeanFactory;
import com.topsion.framework.beans.BeansException;
import com.topsion.framework.beans.SimpleBeanFactory;
import com.topsion.framework.beans.XmlBeanDefinitionReader;
import com.topsion.framework.core.ClassPathXmlResource;
import com.topsion.framework.core.Resource;

/**
 * 1. 解析 XML 文件中的内容。
 * 2. 加载解析的内容，构建 BeanDefinition。
 * 3. 读取 BeanDefinition 的配置信息，实例化 Bean，然后把它注入到 BeanFactory 容器中。
 */
public class ClassPathXmlApplicationContext implements BeanFactory {

    BeanFactory beanFactory;

    public ClassPathXmlApplicationContext(String fileName) {
        Resource resource = new ClassPathXmlResource(fileName);
        this.beanFactory = new SimpleBeanFactory();
        XmlBeanDefinitionReader xmlBeanDefinitionReader = new XmlBeanDefinitionReader(beanFactory);
        xmlBeanDefinitionReader.loadBeanDefinitions(resource);
    }

    @Override
    public Object getBean(String beanName) throws BeansException {
        return this.beanFactory.getBean(beanName);
    }

    @Override
    public void registerBeanDefinition(BeanDefinition beanDefinition) {
        this.beanFactory.registerBeanDefinition(beanDefinition);
    }
}
