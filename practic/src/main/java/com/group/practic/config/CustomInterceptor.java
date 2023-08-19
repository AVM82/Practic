package com.group.practic.config;

import com.group.practic.service.PersonService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.HandlerInterceptor;

@RestController
public class CustomInterceptor implements HandlerInterceptor {

    private final PersonService personService;

    public CustomInterceptor(PersonService personService) {
        this.personService = personService;
    }

    @Override
    public boolean preHandle(
            HttpServletRequest request, HttpServletResponse response, Object handler) {
        personService.createUserIfNotExists();
        return true;
    }
}