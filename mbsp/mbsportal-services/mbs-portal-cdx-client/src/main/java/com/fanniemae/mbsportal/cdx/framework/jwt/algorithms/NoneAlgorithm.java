package com.fanniemae.mbsportal.cdx.framework.jwt.algorithms;

import com.fanniemae.mbsportal.cdx.framework.jwt.exceptions.SignatureGenerationException;
import com.fanniemae.mbsportal.cdx.framework.jwt.exceptions.SignatureVerificationException;
import com.fanniemae.mbsportal.cdx.framework.jwt.interfaces.DecodedJWT;

import org.apache.commons.codec.binary.Base64;

class NoneAlgorithm extends Algorithm {

    NoneAlgorithm() {
        super("none", "none");
    }

    @Override
    public void verify(DecodedJWT jwt) throws SignatureVerificationException {
        byte[] signatureBytes = Base64.decodeBase64(jwt.getSignature());
        if (signatureBytes.length > 0) {
            throw new SignatureVerificationException(this);
        }
    }

    @Override
    public byte[] sign(byte[] contentBytes) throws SignatureGenerationException {
        return new byte[0];
    }
}
