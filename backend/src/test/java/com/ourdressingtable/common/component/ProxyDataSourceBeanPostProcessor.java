package com.ourdressingtable.common.component;

import com.ourdressingtable.chat.ChatroomServicePerformanceTest;
import javax.sql.DataSource;
import net.ttddyy.dsproxy.support.ProxyDataSource;
import net.ttddyy.dsproxy.support.ProxyDataSourceBuilder;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;

@Component
public class ProxyDataSourceBeanPostProcessor implements BeanPostProcessor {

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName)
            throws BeansException {
        if (bean instanceof DataSource && !(bean instanceof ProxyDataSource)) {
            return ProxyDataSourceBuilder
                    .create((DataSource) bean)
                    .name("proxy-ds")
                    .countQuery()
                    .build();
        }
        return bean;
    }
}
