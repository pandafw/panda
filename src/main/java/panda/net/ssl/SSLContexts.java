package panda.net.ssl;

import java.io.IOException;
import java.security.GeneralSecurityException;

import javax.net.ssl.KeyManager;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;

/**
 * General utilities for SSLContext.
 */
public class SSLContexts {
	public static final String PROTO_TLS = "TLS";

	private SSLContexts() {
		// Not instantiable
	}

	/**
	 * Create and initialise an SSLContext.
	 * 
	 * @param protocol the protocol used to instatiate the context
	 * @param keyManager the key manager, may be {@code null}
	 * @param trustManager the trust manager, may be {@code null}
	 * @return the initialised context.
	 * @throws IOException this is used to wrap any {@link GeneralSecurityException} that occurs
	 */
	public static SSLContext createSSLContext(String protocol, KeyManager keyManager, TrustManager trustManager)
			throws IOException {
		return createSSLContext(protocol, keyManager == null ? null : new KeyManager[] { keyManager },
			trustManager == null ? null : new TrustManager[] { trustManager });
	}

	/**
	 * Create and initialise an SSLContext.
	 * 
	 * @param protocol the protocol used to instatiate the context
	 * @param keyManagers the array of key managers, may be {@code null} but array entries must not
	 *            be {@code null}
	 * @param trustManagers the array of trust managers, may be {@code null} but array entries must
	 *            not be {@code null}
	 * @return the initialised context.
	 * @throws IOException this is used to wrap any {@link GeneralSecurityException} that occurs
	 */
	public static SSLContext createSSLContext(String protocol, KeyManager[] keyManagers, TrustManager[] trustManagers)
			throws IOException {
		SSLContext ctx;
		try {
			ctx = SSLContext.getInstance(protocol);
			ctx.init(keyManagers, trustManagers, /* SecureRandom */null);
		}
		catch (GeneralSecurityException e) {
			IOException ioe = new IOException("Could not initialize SSL context");
			ioe.initCause(e);
			throw ioe;
		}
		return ctx;
	}
}
