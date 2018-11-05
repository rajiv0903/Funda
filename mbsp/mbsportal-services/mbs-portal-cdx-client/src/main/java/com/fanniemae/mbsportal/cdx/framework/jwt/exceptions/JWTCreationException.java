package com.fanniemae.mbsportal.cdx.framework.jwt.exceptions;

@SuppressWarnings("serial")
public class JWTCreationException extends RuntimeException {
    public JWTCreationException(String message, Throwable cause) {
        super(message, cause);
    }
}
