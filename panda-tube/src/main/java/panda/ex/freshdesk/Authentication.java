package panda.ex.freshdesk;

import java.net.Proxy;

import panda.lang.Strings;
import panda.net.http.HttpClient;

public class Authentication {
	private boolean validateSslCert = true;
	private Proxy proxy;
	private String domain;
	private String apikey;
	private String username;
	private String password;
	
	/**
	 * 
	 */
	public Authentication() {
	}

	/**
	 * @param domain
	 */
	public Authentication(String domain) {
		super();
		this.domain = domain;
	}

	/**
	 * @param domain
	 * @param apikey
	 */
	public Authentication(String domain, String apikey) {
		super();
		this.domain = domain;
		this.apikey = apikey;
	}

	/**
	 * @param domain
	 * @param username
	 * @param password
	 */
	public Authentication(String domain, String username, String password) {
		super();
		this.domain = domain;
		this.username = username;
		this.password = password;
	}

	/**
	 * @return the proxy
	 */
	public Proxy getProxy() {
		return proxy;
	}

	/**
	 * @param proxy the proxy to set
	 */
	public void setProxy(Proxy proxy) {
		this.proxy = proxy;
	}

	/**
	 * @return the domain
	 */
	public String getDomain() {
		return domain;
	}

	/**
	 * @param domain the domain to set
	 */
	public void setDomain(String domain) {
		this.domain = domain;
	}

	/**
	 * @return the apikey
	 */
	public String getApikey() {
		return apikey;
	}
	/**
	 * @param apikey the apikey to set
	 */
	public void setApikey(String apikey) {
		this.apikey = apikey;
	}
	/**
	 * @return the username
	 */
	public String getUsername() {
		return username;
	}
	/**
	 * @param username the username to set
	 */
	public void setUsername(String username) {
		this.username = username;
	}
	/**
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}
	/**
	 * @param password the password to set
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * @return the validateSslCert
	 */
	public boolean isValidateSslCert() {
		return validateSslCert;
	}

	/**
	 * @param validateSslCert the validateSslCert to set
	 */
	public void setValidateSslCert(boolean validateSslCert) {
		this.validateSslCert = validateSslCert;
	}

	/** 
	 * add basic authentication to http request
	 * @param hc http client
	 */
	public void authenticateRequest(HttpClient hc, String uri) {
		hc.setDisableSSLv3(true);
		hc.setValidateSslCert(validateSslCert);
		hc.setProxy(proxy);
		hc.getRequest().setUrl(domain + uri);
		if (Strings.isNotEmpty(apikey)) {
			hc.getRequest().setBasicAuthentication(apikey, "x");
		}
		else {
			hc.getRequest().setBasicAuthentication(username, password);
		}
	}
	
	
}
