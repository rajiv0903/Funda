package com.fanniemae.mbsportal.cdx.framework.jwt.exceptions;


@SuppressWarnings("serial")
public class InvalidClaimException extends JWTVerificationException {
    public InvalidClaimException(String message) {
        super(message);
    }
}
