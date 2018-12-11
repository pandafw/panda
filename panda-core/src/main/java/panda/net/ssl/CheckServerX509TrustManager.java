package panda.net.ssl;

import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.X509TrustManager;

public class CheckServerX509TrustManager extends TrustedX509TrustManager {
	public static final X509TrustManager INSTANCE = new CheckServerX509TrustManager();

	@Override
	public void checkServerTrusted(X509Certificate[] certificates, String authType) throws CertificateException {
		for (X509Certificate certificate : certificates) {
			certificate.checkValidity();
		}
	}
}
