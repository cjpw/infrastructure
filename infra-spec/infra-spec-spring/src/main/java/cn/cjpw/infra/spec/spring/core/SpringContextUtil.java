package cn.cjpw.infra.spec.spring.core;

import org.springframework.beans.factory.ListableBeanFactory;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.ApplicationContext;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * spring上下文工具
 *
 * @author chenjun
 */
public class SpringContextUtil {

    /**
     * "@PostConstruct"注解标记的类中，由于ApplicationContext还未加载，导致空指针
     * <br>
     * 因此实现BeanFactoryPostProcessor注入ConfigurableListableBeanFactory实现bean的操作
     */
    private static ConfigurableListableBeanFactory beanFactory;
    /**
     * Spring应用上下文环境
     */
    private static ApplicationContext applicationContext;

    public static void setApplicationContext(ApplicationContext applicationContext) {
        SpringContextUtil.applicationContext = applicationContext;
    }

    public static void setBeanFactory(ConfigurableListableBeanFactory beanFactory) {
        SpringContextUtil.beanFactory = beanFactory;
    }

    public static ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    public static ListableBeanFactory getBeanFactory() {
        return null == beanFactory ? applicationContext : beanFactory;
    }

    /**
     * 根据实例beanname获取实例
     *
     * @param name
     * @return
     */
    public static Object getBean(String name) {
        return getBeanFactory().getBean(name);
    }

    /**
     * 根据实例继承关系的class获取实例，只允许实例只有唯一继承实现
     *
     * @param requiredType
     * @param <T>
     * @return
     */
    public static <T> T getBean(Class<T> requiredType) {
        return getBeanFactory().getBean(requiredType);
    }

    /**
     * 根据实例beanname和继承关系的class获取实例，用于获取多子类实例
     *
     * @param name
     * @param requiredType
     * @param <T>
     * @return
     */
    public static <T> T getBean(String name, Class<T> requiredType) {
        return getBeanFactory().getBean(name, requiredType);
    }

    /**
     * 根据实例继承关系的class获取实例,key为beanname
     *
     * @param requiredType
     * @param <T>
     * @return
     */
    public static <T> Map<String, T> getBeansByType(Class<T> requiredType) {
        return getBeanFactory().getBeansOfType(requiredType);
    }

    /**
     * 根据实例继承关系的class获取实例,key为自定义的Funtion转化
     *
     * @param requiredType
     * @param keyFunc
     * @param <T>
     * @param <R>
     * @return
     */
    public static <T, R> Map<R, T> getBeansByType(Class<T> requiredType, Function<T, R> keyFunc) {
        Map<String, T> beansOfType = getBeanFactory().getBeansOfType(requiredType);
        Map<R, T> map = new HashMap<>();
        beansOfType.forEach((k, v) -> {
            map.put(keyFunc.apply(v), v);
        });
        return map;
    }


    /**
     * 判断beanname对应实例是否被容器加载
     *
     * @param name
     * @return
     */
    public static boolean containsBean(String name) {
        return getBeanFactory().containsBean(name);
    }

    /**
     * 是否在容器中是单例实现
     *
     * @param name
     * @return
     */
    public static boolean isSingleton(String name) {
        return getBeanFactory().isSingleton(name);
    }

    /**
     * 根据beanname获取对象实例类型
     *
     * @param name
     * @return
     */
    public static Class<? extends Object> getType(String name) {
        return getBeanFactory().getType(name);
    }


}