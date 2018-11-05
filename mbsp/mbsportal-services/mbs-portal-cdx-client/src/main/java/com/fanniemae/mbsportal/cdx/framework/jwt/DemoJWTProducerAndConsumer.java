package com.fanniemae.mbsportal.cdx.framework.jwt;

import java.security.interfaces.RSAPublicKey;
import java.util.Collection;
import java.util.Iterator;

import com.fanniemae.mbsportal.cdx.framework.jwt.algorithms.Algorithm;
import com.fanniemae.mbsportal.cdx.framework.jwt.interfaces.Claim;
import com.fanniemae.mbsportal.cdx.framework.jwt.interfaces.DecodedJWT;

public class DemoJWTProducerAndConsumer {

    @SuppressWarnings("deprecation")
    public static void main(String a[]) {
        // JWTCreator.Builder builder = JWT.create();
        // builder.withClaim("firstName", "Satish");
        // builder.withClaim("lastName", "Tiyyagura");
        // builder.withClaim("emailAddress", "tiyyagurasatish@gmail.com");
        // builder.withClaim("mobileNumber", "9803098359");
        // builder.withClaim("workNumber", "9726567123");
        // builder.withClaim("cutstomerName", "WELLS FARGO");
        // builder.withClaim("userName", "sxus5t");
        // builder.withClaim("sellerSerivcerNo", 123);
        // builder.withClaim("dealerOrgName", "WELLS FARGO");
        // builder.withClaim("dealerOrgId", 123);
        // builder.withClaim("fannieMaeUser", true);
        // builder.withArrayClaim("roles", new String[] { "Role1, Role2, Role3"
        // });//Roles //TODO
        // builder.withClaim("institutionId", 123);
        // builder.withClaim("defaultSellerServicerNo", 123);
        // RSAPrivateKey privateKey = null;
        // RSAPublicKey publicKey = null;
        //
        // privateKey = (RSAPrivateKey)
        // RSAUtils.getPrivateKeyFromFile("C://Project//keypair//keypair//server.key",
        // "RSA");
        // publicKey = (RSAPublicKey)
        // RSAUtils.getPublicKeyFromFile("C:\\Rajiv\\mbs\\cdx-gateway-certs\\dev-cdxapi.chain.crt");

        // String sginedToken = builder.sign(Algorithm.RSA256(publicKey,
        // privateKey));

        // System.out.println("Sgined JWT Token: " + sginedToken);

        // DecodedJWT jwt =
        // JWT.require(Algorithm.RSA256(publicKey)).build().verify(sginedToken);
        //
        // System.out.println(
        // "Decoded Payload Retrieved Here from the token: " +
        // jwt.getClaim("emailAddress").asString());

        String sginedToken = "eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiJ9.eyJNT0JJTEVfTlVNQkVSIjoiKDIwMikgNzUyLTY2MzIiLCJERUZBVUxUX1NFTExFUl9TRVJWSUNFX05VTUJFUiI6IiIsIkZJUlNUTkFNRSI6Ik1CU1AiLCJVU0VSX05BTUUiOiJwM2hicm1uciIsIkVNQUlMIjoiamVzc2ljYV9kX3NjaHJpZXJAZmFubmllbWFlLmNvbSIsIkRFQUxFUl9PUkdfTkFNRSI6Ik1PVkVNRU5UIE1PUlRHQUdFLCBMTEMiLCJJTlNUSVRVVElPTl9JRCI6IiIsIkNVU1RPTUVSX05BTUUiOiIiLCJMQVNUTkFNRSI6IlJlYWRPbmx5IiwiV09SS19OVU1CRVIiOiIoMjAyKSA3NTItNjYzMiIsIlJPTEVTIjpbIk1CUyBUcmFkZSAtIFJlYWQgT25seSJdLCJTRUxMRVJfU0VSVklDRV9OVU1CRVIiOiIyODU3NyIsIkRFQUxFUl9PUkdfSUQiOiI3MDg5OTQiLCJGQU5OSUVNQUVfVVNFUiI6ZmFsc2UsImV4cCI6MTUwNzUwOTgyMCwiaWF0IjoxNTA3NTA5NjQwfQ.XCWXdCuYd4yrqdkMHvLAHWzfll_Xb9v6TyoC5Dq0zm04Ryx0esVCiURdct7MkK-oMx6dr9XkkAlvPI52AyIN5L4u37Z8gn1zRHP6nyKX6uM4uZazcMHxz2DNmmzCP2xrZAJrWmszdsfEyP0wI1mloVn0dcUqDEEt0oZHYFnZ9aG3LusZp1XCwn_ksi1DH64ddKOUe-2sKeVHDfcJ6Q6iaRSH9C8vqhVGq7k_iqZKMjdPlOeqtBW1ADtkUQkZdYKq6f-Fd_Ph8EBt5QQl51t0kL5Qc9N3d4YL7wv-7WEwSksyLHBnGHC74aQwBYUbmInEUzpGsGtveRX1UL5ENSxKPA";

        RSAPublicKey publicKey = (RSAPublicKey) RSAUtils
                .getPublicKeyFromFile("C:\\Rajiv\\mbs\\cdx-gateway-certs\\dev-cdxapi.chain.crt");

        DecodedJWT jwt = JWT.require(Algorithm.RSA256(publicKey)).build().verify(sginedToken.trim());

        System.out.println("Payload:" + jwt.getClaims().values());
        Collection<String> collection1 = jwt.getClaims().keySet();
        for (Iterator<String> iterator = collection1.iterator(); iterator.hasNext();) {
            String Key = (String) iterator.next();
            System.out.println("Key:" + Key);
        }

        Collection<Claim> collection = jwt.getClaims().values();
        for (Iterator<Claim> iterator = collection.iterator(); iterator.hasNext();) {
            Claim claim = (Claim) iterator.next();
            System.out.println("Claim:" + claim.asString());
        }
        System.out.println("Decoded Payload Retrieved Here from the token: " + jwt.getClaim("EMAIL").asString());

        System.out.println(
                "Decoded Payload Retrieved Here from the token: " + jwt.getClaim("ROLES").asArray(Object.class));

        Object[] roles = jwt.getClaim("ROLES").asArray(Object.class);
        for (Object role : roles) {
            System.out.println(role.getClass());
            System.out.println(role);
        }
    }

}
