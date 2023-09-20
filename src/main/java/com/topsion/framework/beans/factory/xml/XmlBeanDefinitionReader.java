package com.topsion.framework.beans.factory.xml;

import com.topsion.framework.beans.PropertyValue;
import com.topsion.framework.beans.PropertyValues;
import com.topsion.framework.beans.factory.config.BeanDefinition;
import com.topsion.framework.beans.factory.config.ConstructorArgumentValue;
import com.topsion.framework.beans.factory.config.ConstructorArgumentValues;
import com.topsion.framework.beans.factory.support.AbstractBeanFactory;
import com.topsion.framework.core.Resource;
import org.dom4j.Element;

import java.util.ArrayList;
import java.util.List;

public class XmlBeanDefinitionReader {
    AbstractBeanFactory factory;

    public XmlBeanDefinitionReader(AbstractBeanFactory simpleBeanFactory) {
        this.factory = simpleBeanFactory;
    }

    public void loadBeanDefinitions(Resource res) {
        while (res.hasNext()) {
            Element element = (Element) res.next();
            String beanID = element.attributeValue("id");
            String beanClassName = element.attributeValue("class");
            String initMethodName = element.attributeValue("init-method");

            BeanDefinition beanDefinition = new BeanDefinition(beanID, beanClassName);

            //get constructor
            List<Element> constructorElements = element.elements("constructor-arg");
            ConstructorArgumentValues AVS = new ConstructorArgumentValues();
            for (Element e : constructorElements) {
                String pType = e.attributeValue("type");
                String pName = e.attributeValue("name");
                String pValue = e.attributeValue("value");
                AVS.addArgumentValue(new ConstructorArgumentValue(pType, pName, pValue));
            }
            beanDefinition.setConstructorArgumentValues(AVS);
            //end of handle constructor

            //handle properties
            List<Element> propertyElements = element.elements("property");
            PropertyValues PVS = new PropertyValues();
            List<String> refs = new ArrayList<>();
            for (Element e : propertyElements) {
                String pType = e.attributeValue("type");
                String pName = e.attributeValue("name");
                String pValue = e.attributeValue("value");
                String pRef = e.attributeValue("ref");
                String pV = "";
                boolean isRef = false;
                if (pValue != null && !pValue.equals("")) {
                    isRef = false;
                    pV = pValue;
                } else if (pRef != null && !pRef.equals("")) {
                    isRef = true;
                    pV = pRef;
                    refs.add(pRef);
                }
                PVS.addPropertyValue(new PropertyValue(pType, pName, pV, isRef));
            }
            beanDefinition.setPropertyValues(PVS);
            String[] refArray = refs.toArray(new String[0]);
            beanDefinition.setDependsOn(refArray);
            //end of handle properties

            beanDefinition.setInitMethodName(initMethodName);

            this.factory.registerBeanDefinition(beanID, beanDefinition);
        }
    }
}
