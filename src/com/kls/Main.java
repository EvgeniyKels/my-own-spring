package com.kls;

import org.springfraemwork.context.ApplicationContext;

public class Main {
    public static void main(String[] args) {
        ApplicationContext applicationContext = new ApplicationContext("com.kls");
        ProductService productService = (ProductService) applicationContext.getBean(ProductService.class);
        productService.doSomethingForProfiling();
        applicationContext.close();
    }
}
