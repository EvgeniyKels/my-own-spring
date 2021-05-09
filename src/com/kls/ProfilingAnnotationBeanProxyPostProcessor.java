package com.kls;

import com.kls.custom.annotation.Profiling;
import org.springfraemwork.beans.factory.config.IBeanPostProcessor;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.Map;

public class ProfilingAnnotationBeanProxyPostProcessor implements IBeanPostProcessor {
    private final Map<String, Class<?>> profilingBean = new HashMap<>();
    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) {
        Class<?> aClass = bean.getClass();
        if (aClass.isAnnotationPresent(Profiling.class)) {
            profilingBean.put(beanName, aClass);
        }
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) {
        Class<?> aClass = profilingBean.get(beanName);
        if (aClass != null) {
            return Proxy.newProxyInstance(
                    aClass.getClassLoader(),
                    aClass.getInterfaces(),
                    get(bean)
            );
        }
        return bean;
    }

    private InvocationHandler get(Object bean) {
        return (proxy, method, args) -> {
            System.out.println(">>> Profiling");
            long before = System.nanoTime();
            Object retVal = method.invoke(bean, args);
            System.out.println(System.nanoTime() - before);
            System.out.println(">>> End profiling");
            return retVal;
        };
    }

}