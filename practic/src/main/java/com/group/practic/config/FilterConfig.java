package com.group.practic.config;

import com.group.practic.controller.StaticContentFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FilterConfig {

    @Bean
    public FilterRegistrationBean<StaticContentFilter> staticContentFilter() {
        FilterRegistrationBean<StaticContentFilter> registrationBean
                = new FilterRegistrationBean<>();
        registrationBean.setFilter(new StaticContentFilter());
        registrationBean.addUrlPatterns("/*");
        return registrationBean;
    }
}
