package panda.ex.zendesk;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.Proxy;
import java.sql.Date;
import java.util.List;
import java.util.Map;

import panda.bind.adapter.DateAdapter;
import panda.bind.json.JsonDeserializer;
import panda.bind.json.JsonSerializer;
import panda.bind.json.Jsons;
import panda.io.MimeTypes;
import panda.lang.Arrays;
import panda.lang.Strings;
import panda.lang.time.DateTimes;
import panda.net.http.HttpClient;
import panda.net.http.HttpResponse;
import panda.net.http.HttpStatus;
import panda.net.ssl.SSLProtocols;

public class Zendesk {
	public static final String TYPE_PROBLEM = "problem";
	public static final String TYPE_INCIDENT = "incident";
	public static final String TYPE_QUESTION = "question";
	public static final String TYPE_TASK = "task";

	public static final String STATUS_NEW = "new";
	public static final String STATUS_OPEN = "open";
	public static final String STATUS_PENDING = "pending";
	public static final String STATUS_HOLD = "hold";
	public static final String STATUS_SOLVED = "solved";
	public static final String STATUS_CLOSED = "closed";
	
	public static final String PRIORITY_LOW = "low";
	public static final String PRIORITY_NORMAL = "normal";
	public static final String PRIORITY_HIGH = "high";
	public static final String PRIORITY_URGENT = "urgent";
	
	public static final String ROLE_ENDUSER = "end-user";
	public static final String ROLE_AGENT = "agent";
	public static final String ROLE_ADMIN = "admin";
	
	public static final String RESTRICT_ORGANIZATION = "organization";
	public static final String RESTRICT_GROUPS = "groups";
	public static final String RESTRICT_ASSIGNED = "assigned";
	public static final String RESTRICT_REQUESTED = "requested";

	public static final String API_TICKETS = "/api/v2/tickets.json";
	public static final String API_GET_TICKET = "/api/v2/tickets/%d.json";
	public static final String API_GETS_TICKET = "/api/v2/tickets/show_many.json";

	public static final String API_LIST_USERS = "/api/v2/users.json";
	public static final String API_LIST_GROUP_USERS = "/api/v2/groups/%d/users.json";
	public static final String API_LIST_ORANIZATION_USERS = "/api/v2/organizations/%d/users.json";
	
	private boolean validateSslCert = true;
	private Proxy proxy;
	private String domain;
	private String apitoken;
	private String username;
	private String password;
	
	/**
	 * 
	 */
	public Zendesk() {
	}

	/**
	 * @param domain
	 */
	public Zendesk(String domain) {
		this.domain = domain;
	}

	/**
	 * @param domain
	 * @param apitoken
	 */
	public Zendesk(String domain, String apitoken) {
		this.domain = domain;
		this.apitoken = apitoken;
	}

	/**
	 * @param domain
	 * @param username
	 * @param password
	 */
	public Zendesk(String domain, String username, String password) {
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
	 * @return the api token
	 */
	public String getApitoken() {
		return apitoken;
	}
	/**
	 * @param apitoken the api token to set
	 */
	public void setApitoken(String apitoken) {
		this.apitoken = apitoken;
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
		if (Strings.isNotEmpty(apitoken)) {
			hc.getRequest().setBasicAuthentication(apitoken, "x");
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

	public UserListResult listUsers(String...roles) throws ZendeskException {
		HttpClient hc = new HttpClient();
		
		authenticateRequest(hc, API_LIST_USERS);
		hc.getRequest().setContentType(MimeTypes.APP_JSON);
		if (Arrays.isNotEmpty(roles)) {
			hc.getRequest().addParam("role[]", roles);
		}

		try {
			HttpResponse hr = hc.doGet();
			if (hr.isOK()) {
				UserListResult ulr = deserialize(hr.getContentText(), UserListResult.class);
				return ulr;
			}
			
			throw new ZendeskException("Failed to list users: " + hr.getStatusLine() + "\n" + hr.getContentText());
		}
		catch (ZendeskException e) {
			throw e;
		}
		catch (IOException e) {
			throw new ZendeskException("Failed to list users: " + e.getMessage(), e);
		}
	}
	
	public TicketListResult listTickets() throws ZendeskException {
		return listTickets(null);
	}
	
	public TicketListResult listTickets(Map<String, Object> params) throws ZendeskException {
		HttpClient hc = new HttpClient();
		
		authenticateRequest(hc, API_TICKETS);
		hc.getRequest().setContentType(MimeTypes.APP_JSON);
		hc.getRequest().setParams(params);

		try {
			HttpResponse hr = hc.doGet();
			if (hr.isOK()) {
				TicketListResult tlr = deserialize(hr.getContentText(), TicketListResult.class);
				return tlr;
			}
			
			throw new ZendeskException("Failed to list tickets: " + hr.getStatusLine() + "\n" + hr.getContentText());
		}
		catch (ZendeskException e) {
			throw e;
		}
		catch (IOException e) {
			throw new ZendeskException("Failed to list tickets: " + e.getMessage(), e);
		}
	}
	
	public Ticket getTicket(long id) throws ZendeskException {
		HttpClient hc = new HttpClient();
		
		authenticateRequest(hc, String.format(API_GET_TICKET, id));
		hc.getRequest().setContentType(MimeTypes.APP_JSON);

		try {
			HttpResponse hr = hc.doGet();
			if (hr.isOK()) {
				TicketData tr = deserialize(hr.getContentText(), TicketData.class);
				return tr.getTicket();
			}
			
			throw new ZendeskException("Failed to get ticket: " + hr.getStatusLine() + "\n" + hr.getContentText());
		}
		catch (ZendeskException e) {
			throw e;
		}
		catch (IOException e) {
			throw new ZendeskException("Failed to get ticket: " + e.getMessage(), e);
		}
	}
	
	public List<Ticket> getTickets(int... ids) throws ZendeskException {
		HttpClient hc = new HttpClient();
		
		authenticateRequest(hc, API_GETS_TICKET);
		hc.getRequest().setContentType(MimeTypes.APP_JSON);
		hc.getRequest().setParam("ids", Strings.join(ids, ','));

		try {
			HttpResponse hr = hc.doGet();
			if (hr.isOK()) {
				TicketListResult tlr = deserialize(hr.getContentText(), TicketListResult.class);
				return tlr.getTickets();
			}
			
			throw new ZendeskException("Failed to get tickets: " + hr.getStatusLine() + "\n" + hr.getContentText());
		}
		catch (ZendeskException e) {
			throw e;
		}
		catch (IOException e) {
			throw new ZendeskException("Failed to get tickets: " + e.getMessage(), e);
		}
	}

	public Ticket createTicket(Ticket ticket) throws ZendeskException {
		String b = Jsons.toJson(new TicketData(ticket));
		
		HttpClient hc = new HttpClient();
		
		authenticateRequest(hc, API_TICKETS);
		hc.getRequest().setContentType(MimeTypes.APP_JSON);
		hc.getRequest().setBody(b);

		try {
			HttpResponse hr = hc.doPost();
			if (hr.getStatusCode() == HttpStatus.SC_CREATED) {
				TicketData td = deserialize(hr.getContentText(), TicketData.class);
				return td.getTicket();
			}
			
			throw new ZendeskException("Failed to create ticket: " + hr.getStatusLine() + "\n" + hr.getContentText());
		}
		catch (ZendeskException e) {
			throw e;
		}
		catch (IOException e) {
			throw new ZendeskException("Failed to create ticket: " + e.getMessage(), e);
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
