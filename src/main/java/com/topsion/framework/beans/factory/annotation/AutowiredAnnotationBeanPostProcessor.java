package com.topsion.framework.beans.factory.annotation;

import com.topsion.framework.beans.BeansException;
import com.topsion.framework.beans.factory.BeanFactory;
import com.topsion.framework.beans.factory.config.BeanPostProcessor;

import java.lang.reflect.Field;

public class AutowiredAnnotationBeanPostProcessor implements BeanPostProcessor {
    private BeanFactory beanFactory;

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        Object result = bean;
        Class<?> clazz = bean.getClass();
        Field[] fields = clazz.getDeclaredFields();
        if (fields != null) {
            for (Field field : fields) {
                //处理带有Autowired注解的字段
                boolean isAutowired = field.isAnnotationPresent(Autowired.class);
                if (isAutowired) {
                    String fieldName = field.getName();
                    if (this.beanFactory != null) {
                        Object autowiredObj = this.beanFactory.getBean(fieldName);
                        //设置属性值，完成注入
                        field.setAccessible(true);
                        try {
                            field.set(bean, autowiredObj);
                        } catch (IllegalAccessException e) {
                            throw new RuntimeException(e);
                        }

                    }
                }

            }

        }

        return result;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) {
        return null;
    }


    public BeanFactory getBeanFactory() {
        return beanFactory;
    }

    @Override
    public void setBeanFactory(BeanFactory beanFactory) {
        this.beanFactory = beanFactory;
    }
}
