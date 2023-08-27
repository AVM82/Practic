package com.group.practic.exception;

import org.springframework.security.core.AuthenticationException;

public class Oauth2AuthenticationProcessingException extends AuthenticationException {
    private static final long serialVersionUID = 3392450042444522832L;

    public Oauth2AuthenticationProcessingException(String msg, Throwable t) {
        super(msg, t);
    }

    public Oauth2AuthenticationProcessingException(String msg) {
        super(msg);
    }
}
