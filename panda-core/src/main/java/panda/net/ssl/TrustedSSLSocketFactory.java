package panda.net.ssl;

public class TrustedSSLSocketFactory extends ExtendSSLSocketFactory {
	public TrustedSSLSocketFactory() {
		initSSLSocketFactory(TrustManagers.acceptAllTrustManager());
	}
}
