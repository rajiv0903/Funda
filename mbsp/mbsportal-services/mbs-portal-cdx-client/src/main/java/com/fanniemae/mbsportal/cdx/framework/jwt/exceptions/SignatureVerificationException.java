package com.fanniemae.mbsportal.cdx.framework.jwt.exceptions;

import com.fanniemae.mbsportal.cdx.framework.jwt.algorithms.Algorithm;

@SuppressWarnings("serial")
public class SignatureVerificationException extends JWTVerificationException {
    public SignatureVerificationException(Algorithm algorithm) {
        this(algorithm, null);
    }

    public SignatureVerificationException(Algorithm algorithm, Throwable cause) {
        super("The Token's Signature resulted invalid when verified using the Algorithm: " + algorithm, cause);
    }
}
