package com.topsion.framework;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ClassPathXmlApplicationContext {
    private List<BeanDefinition> beanDefinitions = new ArrayList<>();
    private Map<String, Object> singletons = new HashMap<>();

    public ClassPathXmlApplicationContext(String fileName) {
        this.readXml(fileName);
        this.instanceBeans();
    }

    private void readXml(String fileName) {
        SAXReader saxReader = new SAXReader();
        URL xmlPath = this.getClass().getClassLoader().getResource(fileName);
        try {
            Document document = saxReader.read(xmlPath);
            Element rootElement = document.getRootElement();
            rootElement.elements().forEach(element -> {
                String beanId = element.attributeValue("id");
                String className = element.attributeValue("class");
                BeanDefinition beanDefinition = new BeanDefinition(beanId, className);
                beanDefinitions.add(beanDefinition);
            });
        } catch (DocumentException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 利用反射创建bean实例, 并缓存在singletons
     */
    private void instanceBeans() {
        for (BeanDefinition beanDefinition : beanDefinitions) {
            try {
                singletons.put(beanDefinition.getId(), Class.forName(beanDefinition.getClassName()).getDeclaredConstructor().newInstance());
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException |
                     NoSuchMethodException | ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * 对外接口，根据bean名称获取bean的实例
     * @param beanName bean 名称
     * @return bean实例
     */
    public Object getBean(String beanName) {
        return singletons.get(beanName);
    }
}
