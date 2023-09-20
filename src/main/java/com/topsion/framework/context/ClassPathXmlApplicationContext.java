package com.topsion.framework.context;

import com.topsion.framework.beans.factory.config.AutowiredCapableBeanFactory;
import com.topsion.framework.beans.factory.config.BeanDefinition;
import com.topsion.framework.beans.factory.BeanFactory;
import com.topsion.framework.beans.BeansException;
import com.topsion.framework.beans.SimpleBeanFactory;
import com.topsion.framework.beans.factory.config.BeanFactoryPostProcessor;
import com.topsion.framework.beans.factory.xml.XmlBeanDefinitionReader;
import com.topsion.framework.core.ClassPathXmlResource;
import com.topsion.framework.core.Resource;

import java.util.ArrayList;
import java.util.List;

/**
 * 1. 解析 XML 文件中的内容。
 * 2. 加载解析的内容，构建 BeanDefinition。
 * 3. 读取 BeanDefinition 的配置信息，实例化 Bean，然后把它注入到 BeanFactory 容器中。
 */
public class ClassPathXmlApplicationContext implements BeanFactory {

    private final boolean isRefresh;
    SimpleBeanFactory beanFactory;

    private final List<BeanFactoryPostProcessor> beanFactoryPostProcessors =
            new ArrayList<BeanFactoryPostProcessor>();

    public ClassPathXmlApplicationContext(String fileName) {
        this(fileName, true);
    }

    public ClassPathXmlApplicationContext(String fileName, boolean isRefresh) {
        this.isRefresh = isRefresh;
        Resource resource = new ClassPathXmlResource(fileName);
        AutowiredCapableBeanFactory autowiredCapableBeanFactory = new AutowiredCapableBeanFactory();
//        this.beanFactory = new SimpleBeanFactory();
        XmlBeanDefinitionReader xmlBeanDefinitionReader = new XmlBeanDefinitionReader(autowiredCapableBeanFactory);
        xmlBeanDefinitionReader.loadBeanDefinitions(resource);
        if (this.isRefresh) {
            this.beanFactory.refresh();
        }
    }

    @Override
    public Object getBean(String beanName) throws BeansException {
        return this.beanFactory.getBean(beanName);
    }

    @Override
    public boolean containsBean(String name) {
        return false;
    }

    @Override
    public boolean isSingleton(String name) {
        return false;
    }

    @Override
    public boolean isPrototype(String name) {
        return false;
    }

    @Override
    public Class<?> getType(String name) {
        return null;
    }

    public void registerBean(BeanDefinition beanDefinition, Object obj) {
        this.beanFactory.registerBean(beanDefinition, null);
    }

    public List getBeanFactoryPostProcessors() {
        return this.beanFactoryPostProcessors;
    }

    public void addBeanFactoryPostProcessor(BeanFactoryPostProcessor postProcessor) {
        this.beanFactoryPostProcessors.add(postProcessor);
    }

    public void refresh() throws BeansException, IllegalStateException { // Register bean processors that intercept bean creation. registerBeanPostProcessors(this.beanFactory); // Initialize other special beans in specific context subclasses. onRefresh(); } private void registerBeanPostProcessors(AutowireCapableBeanFactory beanFactory) { beanFactory.addBeanPostProcessor(new AutowiredAnnotationBeanPostProcessor()); } private void onRefresh() { this.beanFactory.refresh(); }
    }
}
