package com.topsion;

import com.topsion.framework.beans.BeansException;
import com.topsion.framework.context.ClassPathXmlApplicationContext;
import com.topsion.service.AService;

public class App {
    public static void main(String[] args) throws BeansException {
        ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext("spring-bean.xml");
        AService aService = (AService) ctx.getBean("aService");
        aService.sayHello();
    }
}
