package panda.net.ssl;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;

public class HostnameVerifiers {
	private static final HostnameVerifier TRUST = new TrustHostnameVerifier();

	private static class TrustHostnameVerifier implements HostnameVerifier {
		public boolean verify(String host, SSLSession ses) {
			return true;
		}
	}
	
	public static HostnameVerifier trustHostnameVerifier() {
		return TRUST;
	}
}
