package com.topsion.framework.beans.factory.xml;

import com.topsion.framework.beans.factory.config.BeanDefinition;
import com.topsion.framework.beans.*;
import com.topsion.framework.beans.factory.config.ConstructorArgumentValue;
import com.topsion.framework.beans.factory.config.ConstructorArgumentValues;
import com.topsion.framework.core.Resource;
import org.dom4j.Element;

import java.util.ArrayList;
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
            PropertyValues propertyValues = new PropertyValues();
            List<String> refs = new ArrayList<>();
            List<PropertyValue> pvs = propertyElements.stream()
                    .map(e -> {
                        String pType = e.attributeValue("type");
                        String pName = e.attributeValue("name");
                        String pValue = e.attributeValue("value");
                        String pRef = e.attributeValue("ref");
                        String pv = "";
                        boolean isRef = false;
                        if (pValue != null && !pValue.equals("")) {
                            isRef = false;
                            pv = pValue;
                        }
                        if (pRef != null && !pRef.equals("")) {
                            isRef = true;
                            pv = pRef;
                            refs.add(pRef);
                        }
                        return new PropertyValue(pType, pName, pv, isRef);
                    })
                    .toList();
            String[] refArray = refs.toArray(new String[0]);
            propertyValues.addAllPropertyValues(pvs);
            beanDefinition.setPropertyValues(propertyValues);
            beanDefinition.setDependsOn(refArray);

            //构造器属性
            List<Element> constructorArgElements = element.elements("constructor-arg");
            ConstructorArgumentValues constructorArgumentValues = new ConstructorArgumentValues();
            List<ConstructorArgumentValue> arguments = createArgumentValues(constructorArgElements);
            constructorArgumentValues.addArgumentValues(arguments);
            beanDefinition.setConstructorArguments(constructorArgumentValues);
            this.simpleBeanFactory.registerBeanDefinition(beanDefinition);
        }
    }

    private List<ConstructorArgumentValue> createArgumentValues(List<Element> constructorArgElements) {
        List<ConstructorArgumentValue> arguments = constructorArgElements.stream().map(ce -> {
            String pType = ce.attributeValue("type");
            String pName = ce.attributeValue("name");
            String pValue = ce.attributeValue("value");
            return new ConstructorArgumentValue(pValue, pType, pName);
        }).toList();
        return arguments;
    }

    private BeanDefinition createBeanDefinition(Element element) {
        String beanId = element.attributeValue("id");
        String className = element.attributeValue("class");
        BeanDefinition beanDefinition = new BeanDefinition(beanId, className);
        return beanDefinition;
    }
}
