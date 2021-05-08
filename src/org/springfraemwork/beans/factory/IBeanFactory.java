package org.springfraemwork.beans.factory;

import org.springfraemwork.beans.factory.config.IBeanPostProcessor;

import java.util.Map;

public interface IBeanFactory {
    /**
     * scan base package of the app, get all classes, create instances and put then to singleton map
     * @param basePackage - package with classes of our app
     */
    void instantinate(String basePackage);

    /**
     * find class fields with Authowired annotation and inject them by setter
     */
    void populateProperties();

    /**
     * return bean from context
     * @param beanName
     * @return
     */
    Object getBean(String beanName);

    /**
     * iterates through bean map, finds all instances of IBeanNameAware and for this instances set bean name
     */
    void injectBeanName();

    /**
     * iterates through bean map, finds all instances of IBeanFactoryAware and for this instances set link on bean factory
     */
    void injectBeanFactory();

    /**
     * finds in singleton map all beans implemented IInitializingBean and call in this beans afterPropertySet method. Also check if
     */
    void initializeBeans();

    /**
     * serves closing of the context
     */
    void close();

    Map<String, Object> getSingletons();
}
