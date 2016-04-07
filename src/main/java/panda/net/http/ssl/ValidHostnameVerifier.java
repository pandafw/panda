package panda.net.http.ssl;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;

public class ValidHostnameVerifier implements HostnameVerifier {
	public static final ValidHostnameVerifier INSTANCE = new ValidHostnameVerifier();

	public static ValidHostnameVerifier i() {
		return INSTANCE;
	}
	
	public boolean verify(String host, SSLSession ses) {
		return true;
	}
}
