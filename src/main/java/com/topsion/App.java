package com.topsion;

import com.topsion.framework.beans.BeansException;
import com.topsion.framework.context.ClassPathXmlApplicationContext;
import com.topsion.service.AService;
import com.topsion.service.WithAutowiredBaseBaseService;
import com.topsion.service.WithAutowiredBaseService;

public class App {
    public static void main(String[] args) throws BeansException {
      /*  ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext("spring-bean.xml");
        AService aService = (AService) ctx.getBean("aservice");
        aService.sayHello();*/
        sayHelloWithAnnotation();
    }


   static void sayHelloWithAnnotation() {
        ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext("spring-bean.xml");
        AService aService;
        WithAutowiredBaseService bService;
        try {
            //aService = (AService)ctx.getBean("aservice");
            //aService.sayHello();

            bService = (WithAutowiredBaseService)ctx.getBean("baseservice");
            bService.sayHello();
        } catch (BeansException e) {
            e.printStackTrace();
        }
    }
}
