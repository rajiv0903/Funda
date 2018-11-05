package com.fanniemae.mbsportal.cdx.framework.jwt;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.security.KeyFactory;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.security.spec.EncodedKeySpec;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;

import org.bouncycastle.asn1.ASN1EncodableVector;
import org.bouncycastle.asn1.ASN1Integer;
import org.bouncycastle.asn1.ASN1ObjectIdentifier;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.DERNull;
import org.bouncycastle.asn1.DEROctetString;
import org.bouncycastle.asn1.DERSequence;
import org.bouncycastle.asn1.pkcs.PKCSObjectIdentifiers;
import org.bouncycastle.util.io.pem.PemObject;
import org.bouncycastle.util.io.pem.PemReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RSAUtils {

	private static final Logger LOGGER = LoggerFactory.getLogger(RSAUtils.class);

	public static byte[] parsePEMFile(File pemFile) throws IOException {
		LOGGER.info("parsePEMFile - BEGIN");
		if (!pemFile.isFile() || !pemFile.exists()) {
			throw new FileNotFoundException(String.format("The file '%s' doesn't exist.", pemFile.getAbsolutePath()));
		}
		PemReader reader = new PemReader(new FileReader(pemFile));
		PemObject pemObject = reader.readPemObject();
		LOGGER.info("parsePEMFile - END");
		return pemObject.getContent();
	}

	/**
	 * Get PublicKey based on X.509 certificate as input stream
	 * 
	 * @param in
	 * @return
	 */
	public static PublicKey getPublicKeyFromFile(String filePath) {
		LOGGER.info("getPublicKey - filePath {} - BEGIN" + filePath);
		CertificateFactory cf = null;
		X509Certificate cert = null;
		PublicKey publicKey = null;
		try {
			cf = CertificateFactory.getInstance("X.509");
			cert = (X509Certificate) cf.generateCertificate(new FileInputStream(new File(filePath)));
		} catch (CertificateException certException) {
			LOGGER.error("certException while loading the cert from the path: {} with exception message {}", filePath,
					certException.getMessage());
		} catch (FileNotFoundException fileNotFoundException) {
			LOGGER.error("certException while loading the cert from the path: {} with exception message {}", filePath,
					fileNotFoundException.getMessage());
		}
		if (cert != null) {
			publicKey = cert.getPublicKey();
			LOGGER.debug("Public Key Retrieved Successfully");
		}
		LOGGER.info("getPublicKey - filePath {} - END" + filePath);
		return publicKey;
	}

	/**
	 * Get private key based on the certificate as bytes and algorithm to be
	 * used
	 * 
	 * @param keyBytes
	 * @param algorithm
	 * @return
	 */
	private static PrivateKey getPrivateKey(byte[] keyBytes, String algorithm) {
		LOGGER.info("getPrivateKey - using Alg {} - BEGIN" + algorithm);
		PrivateKey privateKey = null;
		try {
			KeyFactory kf = KeyFactory.getInstance(algorithm);
			// Convert to PKCS8 if the private key is not already
			EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(covnertToPKCS8(keyBytes));
			privateKey = kf.generatePrivate(keySpec);
		} catch (NoSuchAlgorithmException algException) {
			LOGGER.error(
					"Could not reconstruct the private key, the given algorithm could not be found. Exception Message {}",
					algException.getMessage());
		} catch (InvalidKeySpecException invalidKeyException) {
			LOGGER.error(
					"Could not reconstruct the private key, the given algorithm could not be found. Exception Message {}",
					invalidKeyException.getMessage());
		}
		LOGGER.info("getPrivateKey - using Alg {} - END" + algorithm);
		return privateKey;
	}

	/**
	 * Gets private key from the given private key file
	 * 
	 * @param filepath
	 * @param algorithm
	 * @return
	 * @throws IOException
	 */
	public static PrivateKey getPrivateKeyFromFile(String filepath, String algorithm) {
		LOGGER.info("getPrivateKey - using Alg {}, file path {} - BEGIN", algorithm, filepath);
		byte[] bytes = null;
		PrivateKey privateKey = null;
		try {
			bytes = RSAUtils.parsePEMFile(new File(filepath));
		} catch (IOException ioException) {
			LOGGER.error("IOException while loading the private key from the path: {} with exception message {}",
					filepath, ioException.getMessage());
		}
		if (bytes != null) {
			privateKey = RSAUtils.getPrivateKey(bytes, algorithm);
		}
		LOGGER.info("getPrivateKey - using Alg {}, file path {} - BEGIN", algorithm, filepath);
		return privateKey;
	}

	private static byte[] covnertToPKCS8(byte[] keyBytes) {
		LOGGER.info("covnertToPKCS8 -  BEGIN");
		ASN1EncodableVector v = new ASN1EncodableVector();
		v.add(new ASN1Integer(0));
		ASN1EncodableVector v2 = new ASN1EncodableVector();
		v2.add(new ASN1ObjectIdentifier(PKCSObjectIdentifiers.rsaEncryption.getId()));
		v2.add(DERNull.INSTANCE);
		v.add(new DERSequence(v2));
		v.add(new DEROctetString(keyBytes));
		ASN1Sequence seq = new DERSequence(v);
		byte[] privateKeyPKCS8 = null;
		try {
			privateKeyPKCS8 = seq.getEncoded("DER");
		} catch (IOException ioException) {
			LOGGER.error(ioException.getCause().getMessage());
		}
		LOGGER.info("covnertToPKCS8 - END");
		return privateKeyPKCS8;
	}

	/**
	 * 
	 * @param filePath
	 * @param alias
	 * @return
	 */
	public static PublicKey getPublicKeyFromKeyStore(String filePath, String alias, String keyStorePassword) {
		LOGGER.info("getPublicKeyFromKeyStore - from File Path {}, - BEGIN", filePath);
		java.security.cert.Certificate cert = null;
		KeyStore keyStore = getKeystore(filePath, keyStorePassword);
		try {
			cert = keyStore.getCertificate(alias);
		} catch (KeyStoreException keyStoreException) {
			LOGGER.error(
					"Key Store Exception while reading public certificate from the key store in the path {}, Cause: {}",
					filePath, keyStoreException.getMessage());
		}
		LOGGER.info("getPublicKeyFromKeyStore - from File Path {}, - END", filePath);
		return cert.getPublicKey();
	}

	/**
	 * 
	 * @param filePath
	 * @param alias
	 * @param privateKeyPass
	 * @return
	 */
	public static PrivateKey getPrivateKeyFromKeyStore(String filePath, String alias, String privateKeyPass,
			String keyStorePass) {
		LOGGER.info("getPrivateKeyFromKeyStore - from File Path {}, - BEGIN", filePath);
		PrivateKey privateKey = null;
		KeyStore keyStore = getKeystore(filePath, keyStorePass);
		try {
			privateKey = (PrivateKey) keyStore.getKey(alias, privateKeyPass.toCharArray());
		} catch (UnrecoverableKeyException recoveryException) {
			LOGGER.error(recoveryException.getCause().getMessage());
		} catch (NoSuchAlgorithmException noSuchAlgException) {
			LOGGER.error(noSuchAlgException.getCause().getMessage());
		} catch (KeyStoreException keyStoreException) {
			LOGGER.error(keyStoreException.getCause().getMessage());
		}
		LOGGER.info("getPrivateKeyFromKeyStore - from File Path {}, - END", filePath);
		return privateKey;
	}

	private static KeyStore getKeystore(String filePath, String keyStorePassword) {

		LOGGER.debug("getKeystore with KeyStore File Path {} - BEGIN", filePath);
		KeyStore keystore = null;
		try {
			keystore = KeyStore.getInstance("JKS");
		} catch (KeyStoreException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			keystore.load(new FileInputStream(new File(filePath)), keyStorePassword.toCharArray());
		} catch (NoSuchAlgorithmException noSuchAlgException) {
			LOGGER.error(noSuchAlgException.getCause().getMessage());
		} catch (CertificateException certException) {
			LOGGER.error(certException.getCause().getMessage());
		} catch (FileNotFoundException fileNotFoundException) {
			LOGGER.error(fileNotFoundException.getCause().getMessage());
		} catch (IOException ioException) {
			LOGGER.error(ioException.getCause().getMessage());
		}
		LOGGER.debug("getKeystore with KeyStore File Path {} - END", filePath);
		return keystore;
	}
}
