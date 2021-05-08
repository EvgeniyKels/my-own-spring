package com.kls;

import org.springfraemwork.beans.factory.annotation.RandomTextAfterInit;
import org.springfraemwork.beans.factory.annotation.RandomTextBeforeInit;
import org.springfraemwork.beans.factory.config.IBeanPostProcessor;

import java.lang.reflect.Field;

public class RandomTextPostProcessor implements IBeanPostProcessor {
    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) {
        Field[] declaredFields = bean.getClass().getDeclaredFields();
        for (Field field : declaredFields) {
            if(field.isAnnotationPresent(RandomTextBeforeInit.class)) {
                field.setAccessible(true);
                try {
                    field.set(bean, "BEFORE!");
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                    return null;
                }
                field.setAccessible(false);
                System.out.println("BEFORE!");
            }
        }
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) {
        Field[] declaredFields = bean.getClass().getDeclaredFields();
        for (Field field : declaredFields) {
            if(field.isAnnotationPresent(RandomTextAfterInit.class)) {
                field.setAccessible(true);
                try {
                    field.set(bean, "AFTER!");
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                    return null;
                }
                field.setAccessible(false);
                System.out.println("AFTER!");
            }
        }
        return bean;
    }
}
