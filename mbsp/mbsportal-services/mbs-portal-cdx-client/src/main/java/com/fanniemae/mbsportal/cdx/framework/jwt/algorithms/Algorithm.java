package com.fanniemae.mbsportal.cdx.framework.jwt.algorithms;

import com.fanniemae.mbsportal.cdx.framework.jwt.exceptions.SignatureGenerationException;
import com.fanniemae.mbsportal.cdx.framework.jwt.exceptions.SignatureVerificationException;
import com.fanniemae.mbsportal.cdx.framework.jwt.interfaces.DecodedJWT;
import com.fanniemae.mbsportal.cdx.framework.jwt.interfaces.ECDSAKeyProvider;
import com.fanniemae.mbsportal.cdx.framework.jwt.interfaces.RSAKeyProvider;

import java.io.UnsupportedEncodingException;
import java.security.interfaces.*;

/**
 * The Algorithm class represents an algorithm to be used in the Signing or Verification process of a Token.
 */
@SuppressWarnings("WeakerAccess")
public abstract class Algorithm {

    private final String name;
    private final String description;

    /**
     * Creates a new Algorithm instance using SHA256withRSA. Tokens specify this as "RS256".
     *
     * @param keyProvider the provider of the Public Key and Private Key for the verify and signing instance.
     * @return a valid RSA256 Algorithm.
     * @throws IllegalArgumentException if the provided Key is null.
     */
    public static Algorithm RSA256(RSAKeyProvider keyProvider) throws IllegalArgumentException {
        return new RSAAlgorithm("RS256", "SHA256withRSA", keyProvider);
    }

    /**
     * Creates a new Algorithm instance using SHA256withRSA. Tokens specify this as "RS256".
     *
     * @param publicKey  the key to use in the verify instance.
     * @param privateKey the key to use in the signing instance.
     * @return a valid RSA256 Algorithm.
     * @throws IllegalArgumentException if both provided Keys are null.
     */
    public static Algorithm RSA256(RSAPublicKey publicKey, RSAPrivateKey privateKey) throws IllegalArgumentException {
        return RSA256(RSAAlgorithm.providerForKeys(publicKey, privateKey));
    }

    /**
     * Creates a new Algorithm instance using SHA256withRSA. Tokens specify this as "RS256".
     *
     * @param key the key to use in the verify or signing instance.
     * @return a valid RSA256 Algorithm.
     * @throws IllegalArgumentException if the Key Provider is null.
     * @deprecated use {@link #RSA256(RSAPublicKey, RSAPrivateKey)} or {@link #RSA256(RSAKeyProvider)}
     */
    @Deprecated
    public static Algorithm RSA256(RSAKey key) throws IllegalArgumentException {
        RSAPublicKey publicKey = key instanceof RSAPublicKey ? (RSAPublicKey) key : null;
        RSAPrivateKey privateKey = key instanceof RSAPrivateKey ? (RSAPrivateKey) key : null;
        return RSA256(publicKey, privateKey);
    }

    /**
     * Creates a new Algorithm instance using SHA384withRSA. Tokens specify this as "RS384".
     *
     * @param keyProvider the provider of the Public Key and Private Key for the verify and signing instance.
     * @return a valid RSA384 Algorithm.
     * @throws IllegalArgumentException if the Key Provider is null.
     */
    public static Algorithm RSA384(RSAKeyProvider keyProvider) throws IllegalArgumentException {
        return new RSAAlgorithm("RS384", "SHA384withRSA", keyProvider);
    }

    /**
     * Creates a new Algorithm instance using SHA384withRSA. Tokens specify this as "RS384".
     *
     * @param publicKey  the key to use in the verify instance.
     * @param privateKey the key to use in the signing instance.
     * @return a valid RSA384 Algorithm.
     * @throws IllegalArgumentException if both provided Keys are null.
     */
    public static Algorithm RSA384(RSAPublicKey publicKey, RSAPrivateKey privateKey) throws IllegalArgumentException {
        return RSA384(RSAAlgorithm.providerForKeys(publicKey, privateKey));
    }

    /**
     * Creates a new Algorithm instance using SHA384withRSA. Tokens specify this as "RS384".
     *
     * @param key the key to use in the verify or signing instance.
     * @return a valid RSA384 Algorithm.
     * @throws IllegalArgumentException if the provided Key is null.
     * @deprecated use {@link #RSA384(RSAPublicKey, RSAPrivateKey)} or {@link #RSA384(RSAKeyProvider)}
     */
    @Deprecated
    public static Algorithm RSA384(RSAKey key) throws IllegalArgumentException {
        RSAPublicKey publicKey = key instanceof RSAPublicKey ? (RSAPublicKey) key : null;
        RSAPrivateKey privateKey = key instanceof RSAPrivateKey ? (RSAPrivateKey) key : null;
        return RSA384(publicKey, privateKey);
    }

    /**
     * Creates a new Algorithm instance using SHA512withRSA. Tokens specify this as "RS512".
     *
     * @param keyProvider the provider of the Public Key and Private Key for the verify and signing instance.
     * @return a valid RSA512 Algorithm.
     * @throws IllegalArgumentException if the Key Provider is null.
     */
    public static Algorithm RSA512(RSAKeyProvider keyProvider) throws IllegalArgumentException {
        return new RSAAlgorithm("RS512", "SHA512withRSA", keyProvider);
    }

    /**
     * Creates a new Algorithm instance using SHA512withRSA. Tokens specify this as "RS512".
     *
     * @param publicKey  the key to use in the verify instance.
     * @param privateKey the key to use in the signing instance.
     * @return a valid RSA512 Algorithm.
     * @throws IllegalArgumentException if both provided Keys are null.
     */
    public static Algorithm RSA512(RSAPublicKey publicKey, RSAPrivateKey privateKey) throws IllegalArgumentException {
        return RSA512(RSAAlgorithm.providerForKeys(publicKey, privateKey));
    }

    /**
     * Creates a new Algorithm instance using SHA512withRSA. Tokens specify this as "RS512".
     *
     * @param key the key to use in the verify or signing instance.
     * @return a valid RSA512 Algorithm.
     * @throws IllegalArgumentException if the provided Key is null.
     * @deprecated use {@link #RSA512(RSAPublicKey, RSAPrivateKey)} or {@link #RSA512(RSAKeyProvider)}
     */
    @Deprecated
    public static Algorithm RSA512(RSAKey key) throws IllegalArgumentException {
        RSAPublicKey publicKey = key instanceof RSAPublicKey ? (RSAPublicKey) key : null;
        RSAPrivateKey privateKey = key instanceof RSAPrivateKey ? (RSAPrivateKey) key : null;
        return RSA512(publicKey, privateKey);
    }

    /**
     * Creates a new Algorithm instance using HmacSHA256. Tokens specify this as "HS256".
     *
     * @param secret the secret to use in the verify or signing instance.
     * @return a valid HMAC256 Algorithm.
     * @throws IllegalArgumentException     if the provided Secret is null.
     * @throws UnsupportedEncodingException if the current Java platform implementation doesn't support the UTF-8 character encoding.
     */
    public static Algorithm HMAC256(String secret) throws IllegalArgumentException, UnsupportedEncodingException {
        return new HMACAlgorithm("HS256", "HmacSHA256", secret);
    }

    /**
     * Creates a new Algorithm instance using HmacSHA384. Tokens specify this as "HS384".
     *
     * @param secret the secret to use in the verify or signing instance.
     * @return a valid HMAC384 Algorithm.
     * @throws IllegalArgumentException     if the provided Secret is null.
     * @throws UnsupportedEncodingException if the current Java platform implementation doesn't support the UTF-8 character encoding.
     */
    public static Algorithm HMAC384(String secret) throws IllegalArgumentException, UnsupportedEncodingException {
        return new HMACAlgorithm("HS384", "HmacSHA384", secret);
    }

    /**
     * Creates a new Algorithm instance using HmacSHA512. Tokens specify this as "HS512".
     *
     * @param secret the secret to use in the verify or signing instance.
     * @return a valid HMAC512 Algorithm.
     * @throws IllegalArgumentException     if the provided Secret is null.
     * @throws UnsupportedEncodingException if the current Java platform implementation doesn't support the UTF-8 character encoding.
     */
    public static Algorithm HMAC512(String secret) throws IllegalArgumentException, UnsupportedEncodingException {
        return new HMACAlgorithm("HS512", "HmacSHA512", secret);
    }

    /**
     * Creates a new Algorithm instance using HmacSHA256. Tokens specify this as "HS256".
     *
     * @param secret the secret bytes to use in the verify or signing instance.
     * @return a valid HMAC256 Algorithm.
     * @throws IllegalArgumentException if the provided Secret is null.
     */
    public static Algorithm HMAC256(byte[] secret) throws IllegalArgumentException {
        return new HMACAlgorithm("HS256", "HmacSHA256", secret);
    }

    /**
     * Creates a new Algorithm instance using HmacSHA384. Tokens specify this as "HS384".
     *
     * @param secret the secret bytes to use in the verify or signing instance.
     * @return a valid HMAC384 Algorithm.
     * @throws IllegalArgumentException if the provided Secret is null.
     */
    public static Algorithm HMAC384(byte[] secret) throws IllegalArgumentException {
        return new HMACAlgorithm("HS384", "HmacSHA384", secret);
    }

    /**
     * Creates a new Algorithm instance using HmacSHA512. Tokens specify this as "HS512".
     *
     * @param secret the secret bytes to use in the verify or signing instance.
     * @return a valid HMAC512 Algorithm.
     * @throws IllegalArgumentException if the provided Secret is null.
     */
    public static Algorithm HMAC512(byte[] secret) throws IllegalArgumentException {
        return new HMACAlgorithm("HS512", "HmacSHA512", secret);
    }

    /**
     * Creates a new Algorithm instance using SHA256withECDSA. Tokens specify this as "ES256".
     *
     * @param keyProvider the provider of the Public Key and Private Key for the verify and signing instance.
     * @return a valid ECDSA256 Algorithm.
     * @throws IllegalArgumentException if the Key Provider is null.
     */
    public static Algorithm ECDSA256(ECDSAKeyProvider keyProvider) throws IllegalArgumentException {
        return new ECDSAAlgorithm("ES256", "SHA256withECDSA", 32, keyProvider);
    }

    /**
     * Creates a new Algorithm instance using SHA256withECDSA. Tokens specify this as "ES256".
     *
     * @param publicKey  the key to use in the verify instance.
     * @param privateKey the key to use in the signing instance.
     * @return a valid ECDSA256 Algorithm.
     * @throws IllegalArgumentException if the provided Key is null.
     */
    public static Algorithm ECDSA256(ECPublicKey publicKey, ECPrivateKey privateKey) throws IllegalArgumentException {
        return ECDSA256(ECDSAAlgorithm.providerForKeys(publicKey, privateKey));
    }

    /**
     * Creates a new Algorithm instance using SHA256withECDSA. Tokens specify this as "ES256".
     *
     * @param key the key to use in the verify or signing instance.
     * @return a valid ECDSA256 Algorithm.
     * @throws IllegalArgumentException if the provided Key is null.
     * @deprecated use {@link #ECDSA256(ECPublicKey, ECPrivateKey)} or {@link #ECDSA256(ECDSAKeyProvider)}
     */
    @Deprecated
    public static Algorithm ECDSA256(ECKey key) throws IllegalArgumentException {
        ECPublicKey publicKey = key instanceof ECPublicKey ? (ECPublicKey) key : null;
        ECPrivateKey privateKey = key instanceof ECPrivateKey ? (ECPrivateKey) key : null;
        return ECDSA256(publicKey, privateKey);
    }

    /**
     * Creates a new Algorithm instance using SHA384withECDSA. Tokens specify this as "ES384".
     *
     * @param keyProvider the provider of the Public Key and Private Key for the verify and signing instance.
     * @return a valid ECDSA384 Algorithm.
     * @throws IllegalArgumentException if the Key Provider is null.
     */
    public static Algorithm ECDSA384(ECDSAKeyProvider keyProvider) throws IllegalArgumentException {
        return new ECDSAAlgorithm("ES384", "SHA384withECDSA", 48, keyProvider);
    }

    /**
     * Creates a new Algorithm instance using SHA384withECDSA. Tokens specify this as "ES384".
     *
     * @param publicKey  the key to use in the verify instance.
     * @param privateKey the key to use in the signing instance.
     * @return a valid ECDSA384 Algorithm.
     * @throws IllegalArgumentException if the provided Key is null.
     */
    public static Algorithm ECDSA384(ECPublicKey publicKey, ECPrivateKey privateKey) throws IllegalArgumentException {
        return ECDSA384(ECDSAAlgorithm.providerForKeys(publicKey, privateKey));
    }

    /**
     * Creates a new Algorithm instance using SHA384withECDSA. Tokens specify this as "ES384".
     *
     * @param key the key to use in the verify or signing instance.
     * @return a valid ECDSA384 Algorithm.
     * @throws IllegalArgumentException if the provided Key is null.
     * @deprecated use {@link #ECDSA384(ECPublicKey, ECPrivateKey)} or {@link #ECDSA384(ECDSAKeyProvider)}
     */
    @Deprecated
    public static Algorithm ECDSA384(ECKey key) throws IllegalArgumentException {
        ECPublicKey publicKey = key instanceof ECPublicKey ? (ECPublicKey) key : null;
        ECPrivateKey privateKey = key instanceof ECPrivateKey ? (ECPrivateKey) key : null;
        return ECDSA384(publicKey, privateKey);
    }

    /**
     * Creates a new Algorithm instance using SHA512withECDSA. Tokens specify this as "ES512".
     *
     * @param keyProvider the provider of the Public Key and Private Key for the verify and signing instance.
     * @return a valid ECDSA512 Algorithm.
     * @throws IllegalArgumentException if the Key Provider is null.
     */
    public static Algorithm ECDSA512(ECDSAKeyProvider keyProvider) throws IllegalArgumentException {
        return new ECDSAAlgorithm("ES512", "SHA512withECDSA", 66, keyProvider);
    }

    /**
     * Creates a new Algorithm instance using SHA512withECDSA. Tokens specify this as "ES512".
     *
     * @param publicKey  the key to use in the verify instance.
     * @param privateKey the key to use in the signing instance.
     * @return a valid ECDSA512 Algorithm.
     * @throws IllegalArgumentException if the provided Key is null.
     */
    public static Algorithm ECDSA512(ECPublicKey publicKey, ECPrivateKey privateKey) throws IllegalArgumentException {
        return ECDSA512(ECDSAAlgorithm.providerForKeys(publicKey, privateKey));
    }

    /**
     * Creates a new Algorithm instance using SHA512withECDSA. Tokens specify this as "ES512".
     *
     * @param key the key to use in the verify or signing instance.
     * @return a valid ECDSA512 Algorithm.
     * @throws IllegalArgumentException if the provided Key is null.
     * @deprecated use {@link #ECDSA512(ECPublicKey, ECPrivateKey)} or {@link #ECDSA512(ECDSAKeyProvider)}
     */
    @Deprecated
    public static Algorithm ECDSA512(ECKey key) throws IllegalArgumentException {
        ECPublicKey publicKey = key instanceof ECPublicKey ? (ECPublicKey) key : null;
        ECPrivateKey privateKey = key instanceof ECPrivateKey ? (ECPrivateKey) key : null;
        return ECDSA512(publicKey, privateKey);
    }


    public static Algorithm none() {
        return new NoneAlgorithm();
    }

    protected Algorithm(String name, String description) {
        this.name = name;
        this.description = description;
    }

    /**
     * Getter for the Id of the Private Key used to sign the tokens. This is usually specified as the `kid` claim in the Header.
     *
     * @return the Key Id that identifies the Signing Key or null if it's not specified.
     */
    public String getSigningKeyId() {
        return null;
    }

    /**
     * Getter for the name of this Algorithm, as defined in the JWT Standard. i.e. "HS256"
     *
     * @return the algorithm name.
     */
    public String getName() {
        return name;
    }

    /**
     * Getter for the description of this Algorithm, required when instantiating a Mac or Signature object. i.e. "HmacSHA256"
     *
     * @return the algorithm description.
     */
    String getDescription() {
        return description;
    }

    @Override
    public String toString() {
        return description;
    }

    /**
     * Verify the given token using this Algorithm instance.
     *
     * @param jwt the already decoded JWT that it's going to be verified.
     * @throws SignatureVerificationException if the Token's Signature is invalid, meaning that it doesn't match the signatureBytes, or if the Key is invalid.
     */
    public abstract void verify(DecodedJWT jwt) throws SignatureVerificationException;

    /**
     * Sign the given content using this Algorithm instance.
     *
     * @param contentBytes an array of bytes representing the base64 encoded content to be verified against the signature.
     * @return the signature in a base64 encoded array of bytes
     * @throws SignatureGenerationException if the Key is invalid.
     */
    public abstract byte[] sign(byte[] contentBytes) throws SignatureGenerationException;
}
