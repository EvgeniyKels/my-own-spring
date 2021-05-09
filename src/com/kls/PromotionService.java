package com.kls;

import com.kls.custom.annotation.Profiling;
import org.springfraemwork.beans.factory.*;
import org.springfraemwork.beans.factory.annotation.RandomTextAfterInit;
import org.springfraemwork.beans.factory.annotation.RandomTextBeforeInit;
import org.springfraemwork.beans.factory.stereotype.Service;
import org.springfraemwork.context.IApplicationListener;
import org.springfraemwork.context.event.ContextClosedEvent;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.time.LocalDateTime;

@Service
@Profiling
public class PromotionService implements IBeanNameAware, IBeanFactoryAware, IInitializingBean, IDisposableBean, IApplicationListener<ContextClosedEvent> {
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

    @PreDestroy
    public void contextClosing() {
        System.out.println("Context closing");
    }

    @Override
    public void destroy() {
        System.out.println("Called destroy method for ".concat(this.getClass().getSimpleName()));
    }

    @Override
    public void onApplicationEvent(ContextClosedEvent e) {
        System.out.println(">> ContextClosed EVENT");
    }
}
