package com.github.guiziin227.restspringboot.exception;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ResponseStatus;


/**
 * Exception thrown when an invalid JWT authentication is encountered.
 * This exception is typically used to indicate that the provided JWT token
 * is not valid or has expired.
 */
@ResponseStatus(HttpStatus.FORBIDDEN)
public class InvalidJwtAuthenticationException extends AuthenticationException {
    public InvalidJwtAuthenticationException(String message) {
        super(message);
    }
}
