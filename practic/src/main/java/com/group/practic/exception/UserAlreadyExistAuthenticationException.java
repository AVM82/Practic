package com.group.practic.exception;

import java.io.Serial;
import org.springframework.security.core.AuthenticationException;

public class UserAlreadyExistAuthenticationException extends AuthenticationException {
    @Serial
    private static final long serialVersionUID = 5570981880007077317L;

    public UserAlreadyExistAuthenticationException(final String msg) {
        super(msg);
    }

}
