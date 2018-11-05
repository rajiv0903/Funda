package com.fanniemae.mbsportal.cdx.framework.jwt.exceptions;

@SuppressWarnings("serial")
public class AlgorithmMismatchException extends JWTVerificationException {
    public AlgorithmMismatchException(String message) {
        super(message);
    }
}
