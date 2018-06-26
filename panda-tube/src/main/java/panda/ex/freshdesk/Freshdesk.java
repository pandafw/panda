package panda.ex.freshdesk;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.Proxy;
import java.sql.Date;

import panda.bind.adapter.DateAdapter;
import panda.bind.json.JsonDeserializer;
import panda.bind.json.JsonSerializer;
import panda.bind.json.Jsons;
import panda.io.MimeTypes;
import panda.lang.Strings;
import panda.lang.time.DateTimes;
import panda.net.http.HttpClient;
import panda.net.http.HttpResponse;
import panda.net.http.HttpStatus;
import panda.net.ssl.SSLProtocols;

public class Freshdesk {
	public static final int SOURCE_EMAIL = 1;
	
	public static final int SOURCE_PORTAL = 2;
	
	public static final int SOURCE_PHONE = 3;
	
	public static final int SOURCE_CHAT = 7;
	
	public static final int SOURCE_MOBIHELP = 8;
	
	public static final int SOURCE_FEEDBACK_WIDGET = 9;
	
	public static final int SOURCE_OUTBOUND_EMAIL = 10;
	
	
	public static final int STATUS_OPEN = 1;

	public static final int STATUS_PENDING = 2;

	public static final int STATUS_RESOLVED = 3;

	public static final int STATUS_CLOSED = 4;
	
	public static final int PRIORITY_LOW = 1;

	public static final int PRIORITY_MEDIUM = 2;

	public static final int PRIORITY_HIGH = 3;

	public static final int PRIORITY_URGENT = 4;

	public static final String API_TICKETS = "/api/v2/tickets";
	
	private boolean validateSslCert = true;
	private Proxy proxy;
	private String domain;
	private String apikey;
	private String username;
	private String password;
	
	/**
	 * 
	 */
	public Freshdesk() {
	}

	/**
	 * @param domain
	 */
	public Freshdesk(String domain) {
		this.domain = domain;
	}

	/**
	 * @param domain
	 * @param apikey
	 */
	public Freshdesk(String domain, String apikey) {
		this.domain = domain;
		this.apikey = apikey;
	}

	/**
	 * @param domain
	 * @param username
	 * @param password
	 */
	public Freshdesk(String domain, String username, String password) {
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
	 * @param uri http uri
	 */
	protected void authenticateRequest(HttpClient hc, String uri) {
		hc.setEnabledSslProtocols(SSLProtocols.TLS_ONLY);
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

	protected String serialize(Object o) {
		JsonSerializer js = Jsons.newJsonSerializer();
		js.registerAdapter(Date.class, new DateAdapter(DateTimes.ISO_DATE_TIMEZONE_FORMAT));
		return js.serialize(o);
	}

	protected <T> T deserialize(String s, Type type) {
		JsonDeserializer jd = Jsons.newJsonDeserializer();
		jd.setIgnoreMissingProperty(true);
		return jd.deserialize(s, type);
	}

	public Ticket createTicket(Ticket ticket) throws FreshException {
		String b = serialize(ticket);
		
		HttpClient hc = new HttpClient();
		
		authenticateRequest(hc, API_TICKETS);
		hc.getRequest().setContentType(MimeTypes.APP_JSON);
		hc.getRequest().setBody(b);

		try {
			HttpResponse hr = hc.doPost();
			if (hr.getStatusCode() == HttpStatus.SC_CREATED) {
				Ticket t = deserialize(hr.getContentText(), Ticket.class);
				return t;
			}
			
			if (MimeTypes.APP_JSON.equalsIgnoreCase(hr.getContentType())) {
				ErrorResult er = deserialize(hr.getContentText(), ErrorResult.class);
				throw new FreshException("Failed to create ticket: " + er.getDescription(), er);
			}
			
			throw new FreshException("Failed to create ticket: " + hr.getContentText());
		}
		catch (FreshException e) {
			throw e;
		}
		catch (IOException e) {
			throw new FreshException("Failed to create ticket: " + e.getMessage(), e);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		return Jsons.toJson(this, true);
	}
}
