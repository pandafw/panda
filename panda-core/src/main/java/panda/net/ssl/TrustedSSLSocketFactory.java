package panda.net.ssl;

import java.io.IOException;

import javax.net.ssl.SSLContext;

import panda.lang.Exceptions;

public class TrustedSSLSocketFactory extends ExtendSSLSocketFactory {
	public TrustedSSLSocketFactory() {
		try {
			SSLContext sslctx = SSLContexts.createSSLContext("SSL", null, TrustManagers.acceptAllTrustManager());
			ExtendSSLSocketFactory sslsf = new ExtendSSLSocketFactory(sslctx.getSocketFactory());
			setSslSocketFactory(sslsf);
		}
		catch (IOException e) {
			throw Exceptions.wrapThrow(e);
		}
	}
}
