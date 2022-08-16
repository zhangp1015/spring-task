package com.example.config;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @description:
 * @author: zp
 * @create: 2022-07-20 21:42
 **/
@Component
public class ManageSpringBean implements ApplicationContextAware {

    private static ApplicationContext context;

    public static <T> T getBean(final Class<T> requiredType) {
        return context.getBean(requiredType);
    }

    public static <T> T getBean(final String beanName) {
        @SuppressWarnings("unchecked")
        final T bean = (T) context.getBean(beanName);
        return bean;
    }

    public static <T> Map<String, T> getBeans(final Class<T> requiredType) {
        return context.getBeansOfType(requiredType);
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        context = applicationContext;
    }
}
