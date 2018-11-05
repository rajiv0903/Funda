package com.fanniemae.mbsportal.cdx.framework.jwt.exceptions;

import com.fanniemae.mbsportal.cdx.framework.jwt.algorithms.Algorithm;

public class SignatureGenerationException extends JWTCreationException {
    public SignatureGenerationException(Algorithm algorithm, Throwable cause) {
        super("The Token's Signature couldn't be generated when signing using the Algorithm: " + algorithm, cause);
    }
}
