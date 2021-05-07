package com.kls;

import org.springfraemwork.beans.factory.IBeanFactory;
import org.springfraemwork.beans.factory.IBeanFactoryAware;
import org.springfraemwork.beans.factory.IBeanNameAware;
import org.springfraemwork.beans.factory.IInitializingBean;
import org.springfraemwork.beans.factory.annotation.RandomTextAfterInit;
import org.springfraemwork.beans.factory.annotation.RandomTextBeforeInit;
import org.springfraemwork.beans.factory.stereotype.Service;

import javax.annotation.PostConstruct;
import java.time.LocalDateTime;
import java.util.Date;

@Service
public class PromotionService implements IBeanNameAware, IBeanFactoryAware, IInitializingBean {
    private String beanName;
    private IBeanFactory factory;
    private String afterInitializingPropertiesField;
    @RandomTextBeforeInit
    private String randomTextBefore;
    @RandomTextAfterInit
    private String randomTextAfter;

    @PostConstruct
    public void postConstructMethod(String string) {
        System.out.println(string.concat(" called at ").concat(LocalDateTime.now().toString()));
    }

    @Override
    public void setBeanName(String name) {
        this.beanName = name;
    }

    public String getBeanName() {
        return beanName;
    }

    @Override
    public void setBeanFactory(IBeanFactory beanFactory) {
        this.factory = beanFactory;
    }

    public IBeanFactory getFactory() {
        return factory;
    }

    @Override
    public void afterPropertiesSet() {
        afterInitializingPropertiesField = "it`s after initializing properties field";
        System.out.println("after props set");
    }

    public String getAfterInitializingPropertiesField() {
        return afterInitializingPropertiesField;
    }

    public String getRandomTextBefore() {
        return randomTextBefore;
    }

    public String getRandomTextAfter() {
        return randomTextAfter;
    }
}
