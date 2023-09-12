package com.topsion.framework.beans.factory.support;

import com.topsion.framework.beans.factory.config.BeanDefinition;
import com.topsion.framework.beans.factory.config.SingletonBeanRegistry;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Spring 里学的方法，它作为一个框架并不会把代码写死，
 * 所以这里面的很多实现类都是默认的，默认是什么意思呢？
 * 就是我们可以去替换，不用这些默认的类也是可以的。
 */
public abstract class DefaultSingletonBeanRegistry implements SingletonBeanRegistry {

    protected List<String> beanNames = new ArrayList<>();
    protected final Map<String, Object> singletons = new ConcurrentHashMap<>();


    @Override

    public void registerSingleton(String beanName, Object obj) {
        synchronized (this.singletons) {
            this.singletons.put(beanName, obj);
            this.beanNames.add(beanName);
        }
    }

    @Override
    public Object getSingletonBean(String beanName) {
        return this.singletons.get(beanName);
    }

    @Override
    public boolean containsSingleton(String beanName) {
        return this.singletons.containsKey(beanName);
    }

    @Override
    public String[] getSingletonNames() {
        return this.beanNames.toArray(new String[0]);
    }

    public abstract void registerBeanDefinition(BeanDefinition beanDefinition);

    public abstract boolean containsBeanDefinition(String name);

    public abstract boolean isPrototype(String name);

    public abstract boolean containsBean(String beanName);

    public abstract void registerBeanDefinition(String name, BeanDefinition beanDefinition);

    public abstract BeanDefinition getBeanDefinition(String name);

    public abstract boolean isSingleton(String name);

    public abstract Class getType(String name);
}
