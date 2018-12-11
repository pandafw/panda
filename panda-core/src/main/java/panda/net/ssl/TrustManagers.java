package panda.net.ssl;

import java.security.GeneralSecurityException;
import java.security.KeyStore;

import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

/**
 * TrustManager utilities for generating TrustManagers.
 */
public final class TrustManagers {
	/**
	 * Generate a TrustManager that performs no checks.
	 * 
	 * @return the TrustManager
	 */
	public static X509TrustManager acceptAllTrustManager() {
		return TrustedX509TrustManager.INSTANCE;
	}

	/**
	 * Generate a TrustManager that checks server certificates for validity, but otherwise performs
	 * no checks.
	 * 
	 * @return the validating TrustManager
	 */
	public static X509TrustManager validateServerCertificateTrustManager() {
		return CheckServerX509TrustManager.INSTANCE;
	}

	/**
	 * Return the default TrustManager provided by the JVM.
	 * <p>
	 * This should be the same as the default used by
	 * {@link javax.net.ssl.SSLContext#init(javax.net.ssl.KeyManager[], javax.net.ssl.TrustManager[], java.security.SecureRandom)
	 * SSLContext#init(KeyManager[], TrustManager[], SecureRandom)} when the TrustManager parameter
	 * is set to {@code null}
	 * 
	 * @return the default TrustManager
	 * @throws GeneralSecurityException if an error occurs
	 */
	public static X509TrustManager getDefaultTrustManager() throws GeneralSecurityException {
		return getDefaultTrustManager(null);
	}

	/**
	 * Return the default TrustManager provided by the JVM.
	 * <p>
	 * This should be the same as the default used by
	 * {@link javax.net.ssl.SSLContext#init(javax.net.ssl.KeyManager[], javax.net.ssl.TrustManager[], java.security.SecureRandom)
	 * SSLContext#init(KeyManager[], TrustManager[], SecureRandom)} when the TrustManager parameter
	 * is set to {@code null}
	 * 
	 * @param keyStore the KeyStore to use, may be {@code null}
	 * @return the default TrustManager
	 * @throws GeneralSecurityException if an error occurs
	 */
	public static X509TrustManager getDefaultTrustManager(KeyStore keyStore) throws GeneralSecurityException {
		String defaultAlgorithm = TrustManagerFactory.getDefaultAlgorithm();
		TrustManagerFactory instance = TrustManagerFactory.getInstance(defaultAlgorithm);
		instance.init(keyStore);
		return (X509TrustManager)instance.getTrustManagers()[0];
	}

}
