package com.fanniemae.mbsportal.cdx.framework.jwt.exceptions;

@SuppressWarnings("serial")
public class JWTDecodeException extends JWTVerificationException {
    public JWTDecodeException(String message) {
        this(message, null);
    }

    public JWTDecodeException(String message, Throwable cause) {
        super(message, cause);
    }
}
