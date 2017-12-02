package com.sql.extra;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.util.Assert;

import java.util.Collection;

/**
 * <br/>
 *
 * @author pengc
 * @see com.sql.extra
 * @since 2017/11/30
 */
public class ApplicationContextHelper implements ApplicationContextAware {

    private static ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext)throws BeansException {
        ApplicationContextHelper.applicationContext = applicationContext;
    }

    public static <T> Collection<T> getBeansOfType(Class<T> clazz) {
        return applicationContext.getBeansOfType(clazz).values();
    }

    @SuppressWarnings("unchecked")
    public static <T> T getBean(String name) {
        checkApplicationContext();
        return (T) applicationContext.getBean(name);
    }

    @SuppressWarnings("unchecked")
    public static <T> T getBean(Class<T> clazz) {
        checkApplicationContext();
        return (T) applicationContext.getBeansOfType(clazz);
    }

    private static void checkApplicationContext() {
        Assert.notNull(applicationContext,
                "applicationContext未注入,请在applicationContext.xml中定义ApplicationContextHelper");
    }

}
