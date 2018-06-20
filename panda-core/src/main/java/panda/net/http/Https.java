package panda.net.http;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Map.Entry;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;

import panda.lang.Exceptions;
import panda.net.ssl.HostnameVerifiers;
import panda.net.ssl.SSLContexts;
import panda.net.ssl.SSLSocketFactoryEx;
import panda.net.ssl.TrustManagers;

/**
 * panda.log use this class, so do not use log
 */
public class Https {
	public static void setEnabledProtocals(HttpURLConnection conn, String ... protocols) {
		try {
			if (conn instanceof HttpsURLConnection) {
				HttpsURLConnection sconn = (HttpsURLConnection)conn;
				SSLSocketFactory sslsf = sconn.getSSLSocketFactory();
				SSLSocketFactoryEx sslsfe = new SSLSocketFactoryEx(sslsf, protocols);
				sconn.setSSLSocketFactory(sslsfe);
			}
		}
		catch (Exception e) {
			throw Exceptions.wrapThrow(e);
		}
	}
	
	public static void ignoreValidateCertification(HttpURLConnection conn) {
		try {
			if (conn instanceof HttpsURLConnection) {
				HttpsURLConnection sconn = (HttpsURLConnection)conn;

				SSLContext sslcontext = SSLContexts.createSSLContext("SSL", null, TrustManagers.getAcceptAllTrustManager());
				SSLSocketFactory sslsf = sslcontext.getSocketFactory();
				sconn.setSSLSocketFactory(sslsf);
				sconn.setHostnameVerifier(HostnameVerifiers.trustHostnameVerifier());
			}
		}
		catch (Exception e) {
			throw Exceptions.wrapThrow(e);
		}
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
