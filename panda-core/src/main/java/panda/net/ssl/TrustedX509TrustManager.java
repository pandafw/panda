package panda.net.ssl;

import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.X509TrustManager;

public class TrustedX509TrustManager implements X509TrustManager {
	public static final X509TrustManager INSTANCE = new TrustedX509TrustManager();

	protected static final X509Certificate[] EMPTY_X509CERTIFICATE_ARRAY = new X509Certificate[] {};

	@Override
	public void checkClientTrusted(X509Certificate[] certificates, String authType) {
	}

	@Override
	public void checkServerTrusted(X509Certificate[] certificates, String authType) throws CertificateException {
	}

	/**
	 * @return an empty array of certificates
	 */
	@Override
	public X509Certificate[] getAcceptedIssuers() {
		return EMPTY_X509CERTIFICATE_ARRAY;
	}
}
