package com.group.practic.config;

import com.group.practic.service.PersonService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.view.InternalResourceView;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

@Configuration
public class WebMvcApplicationConfig implements WebMvcConfigurer {

    private final PersonService personService;

    public WebMvcApplicationConfig(PersonService personService) {
        this.personService = personService;
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        String crossOrigins = "*";
        registry.addMapping("/api/**")
                .allowedOrigins(crossOrigins)
                .maxAge(3600);
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new CustomInterceptor(personService))
                .addPathPatterns("/api/persons/profile");
    }

    @Bean
    public ViewResolver internalResourceViewResolver() {
        InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
        viewResolver.setViewClass(InternalResourceView.class);
        return viewResolver;
    }
}
