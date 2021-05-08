package org.springfraemwork.beans.factory;

import org.springfraemwork.beans.factory.annotation.Autowired;
import org.springfraemwork.beans.factory.annotation.Resource;
import org.springfraemwork.beans.factory.config.IBeanPostProcessor;
import org.springfraemwork.beans.factory.stereotype.Component;
import org.springfraemwork.beans.factory.stereotype.Service;

import javax.annotation.PreDestroy;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.*;

public class BeanFactory implements IBeanFactory {
    private final Map<String, Object> singletons = new HashMap<>();
    private final List<IBeanPostProcessor> beansWithPostProcessors = new ArrayList<>();

    @Override
    public Object getBean(String beanName) {
        return singletons.get(beanName);
    }

    @Override
    public void instantinate(String basePackage) {
        ClassLoader classLoader = ClassLoader.getSystemClassLoader();
        String path = basePackage.replace('.', '/');
        //get class`s URIs from classloader
        Enumeration<URL> resources = null;
        try {
            resources = classLoader.getResources(path);
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
        // iterate from all class URIs
        while (resources.hasMoreElements()) {
            URL resource = resources.nextElement();
            File file = null;
            try {
                file = new File(resource.toURI());
            } catch (URISyntaxException e) {
                e.printStackTrace();
                return;
            }
            for (File classFile : Objects.requireNonNull(file.listFiles(), "list files null")) {
                String fileName = classFile.getName();
                // by full name of the class receive object of the class
                if(fileName.endsWith(".class")) {
                    String className = fileName.substring(0, fileName.lastIndexOf("."));
                    Class classObject;
                    try {
                        classObject = Class.forName(basePackage.concat(".").concat(className));
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                        return;
                    }
                    // if annotation @Component or @Service present - creates bean and put it to singletons map
                    if (classObject.isAnnotationPresent(Component.class) || classObject.isAnnotationPresent(Service.class)) {
                        try {
                            Object instance = classObject.newInstance();
                            String beanName = className.substring(0, 1).toLowerCase() + className.substring(1);
                            singletons.put(beanName, instance);
                        } catch (InstantiationException | IllegalAccessException e) {
                            e.printStackTrace();
                            return;
                        }
                    }
                }
            }
        }
    }

    @Override
    public void populateProperties() {
        for (Object object : singletons.values()) {
            for (Field field : object.getClass().getDeclaredFields()) {
                if (field.isAnnotationPresent(Autowired.class)) {
                    for (Object dependency : singletons.values()) {
                        if (dependency.getClass().equals(field.getType())) {
                            String setterName = "set".concat(field.getName().substring(0, 1).toUpperCase()).concat(field.getName().substring(1));
                            injectBeanBySetter(setterName, dependency, object);
                        }
                    }
                } else if (field.isAnnotationPresent(Resource.class)) {
                    for (String dependencyName : singletons.keySet()) {
                        if(dependencyName.equals(field.getName())) {
                            String setterName = "set".concat(field.getName().substring(0, 1).toUpperCase()).concat(field.getName().substring(1));
                            Object dependency = singletons.get(dependencyName);
                            injectBeanBySetter(setterName, dependency, object);
                        }
                    }
                }
            }
        }
    }

    @Override
    public void injectBeanName() {
        for (String beanName : singletons.keySet()) {
            Object bean = singletons.get(beanName);
            if (bean instanceof IBeanNameAware) {
                ((IBeanNameAware) bean).setBeanName(beanName);
            }
        }
    }

    @Override
    public void injectBeanFactory() {
        for (Object bean : singletons.values()) {
            if (bean instanceof IBeanFactoryAware) {
                ((IBeanFactoryAware)bean).setBeanFactory(this);
            }
        }
    }

    @Override
    public void initializeBeans() {
        for (String name : singletons.keySet()) {
            Object bean = singletons.get(name);
            for (IBeanPostProcessor beanPostProcessor : beansWithPostProcessors) {
                beanPostProcessor.postProcessBeforeInitialization(bean, name);
            }
            if (bean instanceof IInitializingBean) {
                ((IInitializingBean)bean).afterPropertiesSet();
            }
            for (IBeanPostProcessor beanPostProcessor : beansWithPostProcessors) {
                beanPostProcessor.postProcessAfterInitialization(bean, name);
            }
        }
    }

    @Override
    public void close() {
        for (Object bean : singletons.values()) {
            if (bean instanceof IDisposableBean) {
                ((IDisposableBean)bean).destroy();
            }
            Method[] methods = bean.getClass().getMethods();
            for (Method method : methods) {
                if (method.isAnnotationPresent(PreDestroy.class)) {
                    try {
                        method.invoke(bean);
                    } catch (IllegalAccessException | InvocationTargetException e) {
                        e.printStackTrace();
                        return;
                    }
                }
            }
        }
    }

    @Override
    public Map<String, Object> getSingletons() {
        return singletons;
    }

    private void injectBeanBySetter(String setterName, Object dependency, Object singleton) {
        Method setter = null;
        try {
            setter = singleton.getClass().getMethod(setterName, dependency.getClass());
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
            return;
        }
        try {
            setter.invoke(singleton, dependency);
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }
}
