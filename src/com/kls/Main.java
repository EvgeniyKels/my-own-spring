package com.kls;

import org.springfraemwork.beans.factory.BeanFactory;
import org.springfraemwork.beans.factory.IBeanFactory;
import org.springfraemwork.context.ApplicationContext;

public class Main {
    public static void main(String[] args) {
        ApplicationContext applicationContext = new ApplicationContext("com.kls");
        applicationContext.close();
    }
}
