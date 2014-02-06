package panda.net.http.ssl;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;

public class ValidHostnameVerifier implements HostnameVerifier {
	public boolean verify(String host, SSLSession ses) {
		return true;
	}
}
