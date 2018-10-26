package panda.net.http;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Map.Entry;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSocketFactory;

import panda.lang.Exceptions;
import panda.lang.Strings;
import panda.net.ssl.HostnameVerifiers;
import panda.net.ssl.ExtendSSLSocketFactory;
import panda.net.ssl.TrustedSSLSocketFactory;

/**
 * panda.log use this class, so do not use log
 */
public class Https {
	public static void setSNIExtension(boolean enable) {
		System.setProperty("jsse.enableSNIExtension", String.valueOf(enable));
	}

	public static void setProtocols(String ... protocols) {
		System.setProperty("https.protocols", Strings.join(protocols, ','));
	}
	
	public static void setProtocols(HttpURLConnection conn, String ... protocols) {
		try {
			if (conn instanceof HttpsURLConnection) {
				HttpsURLConnection sconn = (HttpsURLConnection)conn;
				SSLSocketFactory sslsf = sconn.getSSLSocketFactory();
				if (sslsf instanceof ExtendSSLSocketFactory) {
					((ExtendSSLSocketFactory)sslsf).setEnabledProtocols(protocols);
				}
				else {
					ExtendSSLSocketFactory sslsfe = new ExtendSSLSocketFactory(sslsf);
					sslsfe.setEnabledProtocols(protocols);
					sconn.setSSLSocketFactory(sslsfe);
				}
			}
		}
		catch (Exception e) {
			throw Exceptions.wrapThrow(e);
		}
	}

	public static TrustedSSLSocketFactory setTrustedSSLSocketFactory(HttpsURLConnection sconn) {
		SSLSocketFactory sslsf = sconn.getSSLSocketFactory();
		if (sslsf instanceof TrustedSSLSocketFactory) {
			return ((TrustedSSLSocketFactory)sslsf);
		}
		
		TrustedSSLSocketFactory tsslsf = new TrustedSSLSocketFactory();
		sconn.setSSLSocketFactory(tsslsf);
		return tsslsf;
	}

	public static void disableHostnameCheck(HttpURLConnection conn) {
		if (!(conn instanceof HttpsURLConnection)) {
			return;
		}

		HttpsURLConnection sconn = (HttpsURLConnection)conn;
		setTrustedSSLSocketFactory(sconn);
		sconn.setHostnameVerifier(HostnameVerifiers.trustHostnameVerifier());
	}
	
	public static void disableSniExtension(HttpURLConnection conn) {
		if (!(conn instanceof HttpsURLConnection)) {
			return;
		}

		HttpsURLConnection sconn = (HttpsURLConnection)conn;
		TrustedSSLSocketFactory tsslsf = setTrustedSSLSocketFactory(sconn);
		tsslsf.setSniExtensionDisabled(true);
	}

	public static void setRequestHost(HttpURLConnection conn, URL url) {
		String host = url.getHost();
		if (url.getPort() > 0 && url.getPort() != 80) {
			host += ":" + url.getPort();
		}
		conn.setRequestProperty(HttpHeader.HOST, host);
	}
	
	public static void setRequestHeader(HttpURLConnection conn, HttpHeader header) {
		for (Entry<String, Object> en : header.entrySet()) {
			String key = en.getKey();
			Object val = en.getValue();
			if (val instanceof List) {
				for (Object v : (List)val) {
					conn.addRequestProperty(key, v.toString());
				}
			}
			else {
				conn.addRequestProperty(key, val.toString());
			}
		}
	}
	
	public static void setupRequestHeader(HttpURLConnection conn, URL url, HttpHeader header) {
		setRequestHost(conn, url);
		
		if (header != null) {
			setRequestHeader(conn, header);
		}
	}
}
