package org.springfraemwork.context;

import org.springfraemwork.beans.factory.BeanFactory;
import org.springfraemwork.context.event.ContextClosedEvent;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

public class ApplicationContext {
    private final BeanFactory beanFactory = new BeanFactory();

    public ApplicationContext(String basePackage) {
        System.out.println("************ Context is under construction ************");
        beanFactory.instantinate(basePackage);
        beanFactory.populateProperties();
        beanFactory.injectBeanName();
        beanFactory.injectBeanFactory();
        beanFactory.initializeBeans();
    }

    public void close() {
        beanFactory.close();
        for (Object bean : beanFactory.getSingletons().values()) {
            Type[] genericInterfaces = bean.getClass().getGenericInterfaces();
            for (Type type : genericInterfaces) {
                if (type instanceof ParameterizedType) {
                    ParameterizedType parameterizedType = (ParameterizedType) type;
                    Type actualTypeArgument = parameterizedType.getActualTypeArguments()[0];
                    if (actualTypeArgument.equals(ContextClosedEvent.class)) {
                        Method method = null;
                        try {
                            method = bean.getClass().getMethod("onApplicationEvent", ContextClosedEvent.class);
                        } catch (NoSuchMethodException e) {
                            e.printStackTrace();
                            return;
                        }
                        try {
                            method.invoke(bean, new ContextClosedEvent());
                        } catch (IllegalAccessException | InvocationTargetException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
    }

    public Object getBean(String beanName) {
        return beanFactory.getBean(beanName);
    }

    public Object getBean(Class<?>clazz) {
        return beanFactory.getBean(clazz);
    }
}
