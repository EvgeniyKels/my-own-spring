package com.kls;

import org.springfraemwork.beans.factory.BeanFactory;
import org.springfraemwork.beans.factory.IBeanFactory;
import org.springfraemwork.beans.factory.post_processors.PostConstructAnnotationPostProcessor;
import org.springfraemwork.beans.factory.post_processors.RandomTextPostProcessor;

public class Main {
    public static void main(String[] args) {
        IBeanFactory beanFactory = new BeanFactory();
        beanFactory.instantinate("com.kls");
        beanFactory.populateProperties();
        beanFactory.injectBeanName();
        beanFactory.injectBeanFactory();
        beanFactory.addPostProcessor(new RandomTextPostProcessor());
        beanFactory.addPostProcessor(new PostConstructAnnotationPostProcessor());
        beanFactory.initializeBeans();

        ProductService productService = (ProductService) beanFactory.getBean("productService");
        System.out.println(productService.getPromotionService().getAfterInitializingPropertiesField());
    }
}
