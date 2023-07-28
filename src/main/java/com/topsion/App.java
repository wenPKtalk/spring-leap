package com.topsion;

import com.topsion.framework.ClassPathXmlApplicationContext;
import com.topsion.service.AService;

public class App {
    public static void main(String[] args) {
        ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext("spring-bean.xml");
        AService aService = (AService) ctx.getBean("aService");
        aService.sayHello();
    }
}
