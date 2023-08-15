package com.topsion.framework.beans;

import com.topsion.framework.BeanDefinition;
import com.topsion.framework.core.Resource;
import org.dom4j.Attribute;
import org.dom4j.Element;

import java.util.List;

public class XmlBeanDefinitionReader {
    SimpleBeanFactory simpleBeanFactory;

    public XmlBeanDefinitionReader(SimpleBeanFactory simpleBeanFactory) {
        this.simpleBeanFactory = simpleBeanFactory;
    }

    public void loadBeanDefinitions(Resource resource) {
        while (resource.hasNext()) {
            Element element = (Element) resource.next();
            BeanDefinition beanDefinition = createBeanDefinition(element);
            //创建属性
            List<Element> propertyElements = element.elements("property");
            PropertyValues propertyValues = createPropertyValues(propertyElements);
            //构造器属性
            List<Element> constructorArgElements = element.elements("constructor-arg");
            ArgumentValues argumentValues = new ArgumentValues();
            List<ArgumentValue> arguments = createArgumentValues(constructorArgElements);
            argumentValues.addArgumentValues(arguments);
            beanDefinition.setConstructorArguments(argumentValues);
            this.simpleBeanFactory.registerBeanDefinition(beanDefinition);
        }
    }

    private List<ArgumentValue> createArgumentValues(List<Element> constructorArgElements) {
        List<ArgumentValue> arguments = constructorArgElements.stream().map(ce -> {
            String pType = ce.attributeValue("type");
            String pName = ce.attributeValue("name");
            String pValue = ce.attributeValue("value");
            return new ArgumentValue(pValue, pType, pName);
        }).toList();
        return arguments;
    }

    private PropertyValues createPropertyValues(List<Element> propertyElements) {
        PropertyValues propertyValues = new PropertyValues();
        List<PropertyValue> pvs = propertyElements.stream()
                .map(e -> {
                    String pType = e.attributeValue("type");
                    String pName = e.attributeValue("name");
                    String pValue = e.attributeValue("value");
                    return new PropertyValue(pType, pName, pValue);
                })
                .toList();
        propertyValues.addAllPropertyValues(pvs);
        return propertyValues;
    }

    private BeanDefinition createBeanDefinition(Element element) {
        String beanId = element.attributeValue("id");
        String className = element.attributeValue("class");
        BeanDefinition beanDefinition = new BeanDefinition(beanId, className);
        return beanDefinition;
    }
}
