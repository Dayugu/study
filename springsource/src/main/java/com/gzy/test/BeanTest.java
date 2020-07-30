package com.gzy.test;

import com.gzy.bean.Student;
import org.junit.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

public class BeanTest {

    @Test
    public void testAnnotationLoadBean(){
        AnnotationConfigApplicationContext annotationConfigApplicationContext =
                new AnnotationConfigApplicationContext("com.gzy");
        Student student = (Student)annotationConfigApplicationContext.getBean("student");

        System.out.println("---------------"+student.getName());

    }



    @Test
    public void testXmlLoadBean(){

        ClassPathXmlApplicationContext classPathXmlApplicationContext =
                new ClassPathXmlApplicationContext("spring-context.xml");
        Student student = (Student)classPathXmlApplicationContext.getBean("student");
        System.out.println("---------------"+student.getName());

    }


    @Test
    public void testFileLoadBean(){
        FileSystemXmlApplicationContext fileSystemXmlApplicationContext =
                new FileSystemXmlApplicationContext("F:\\ideaworkspace\\study\\springsource\\src\\main\\resources\\spring-context.xml");
        Student student = (Student)fileSystemXmlApplicationContext.getBean("student");
        System.out.println("---------------"+student.getName());
    }

    @Test
    public void testBootLoadBean(){
        //new EmbeddedWebApplicationContext();
    }
}
