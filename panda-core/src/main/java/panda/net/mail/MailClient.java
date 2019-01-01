package panda.net.mail;

import panda.log.Log;

/**
 * a interface for send mail
 */
public abstract class MailClient {
	protected Log log;

	protected String helo = "localhost";
	protected String host;
	protected int port;
	protected boolean ssl = false;
	protected boolean startTls = true;
	protected boolean debug;
	protected String username;
	protected String password;

	protected int connectTimeout = 5000;
	protected int defaultTimeout = 15000;

	/**
	 * @return the log
	 */
	public Log getLog() {
		return log;
	}

	/**
	 * @param log the log to set
	 */
	public void setLog(Log log) {
		this.log = log;
	}

	/**
	 * @return the helo
	 */
	public String getHelo() {
		return helo;
	}

	/**
	 * @param helo the helo to set
	 */
	public void setHelo(String helo) {
		this.helo = helo;
	}

	/**
	 * @return the host
	 */
	public String getHost() {
		return host;
	}

	/**
	 * @param host the host to set
	 */
	public void setHost(String host) {
		this.host = host;
	}

	/**
	 * @return the port
	 */
	public int getPort() {
		return port;
	}

	/**
	 * @param port the port to set
	 */
	public void setPort(int port) {
		this.port = port;
	}

	/**
	 * @return the ssl
	 */
	public boolean isSsl() {
		return ssl;
	}

	/**
	 * @param ssl the ssl to set
	 */
	public void setSsl(boolean ssl) {
		this.ssl = ssl;
	}

	/**
	 * @return the startTls
	 */
	public boolean isStartTls() {
		return startTls;
	}

	/**
	 * @param startTls the startTls to set
	 */
	public void setStartTls(boolean startTls) {
		this.startTls = startTls;
	}

	/**
	 * @return the debug
	 */
	public boolean isDebug() {
		return debug;
	}

	/**
	 * @param debug the debug to set
	 */
	public void setDebug(boolean debug) {
		this.debug = debug;
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
	 * @return the connectTimeout
	 */
	public int getConnectTimeout() {
		return connectTimeout;
	}

	/**
	 * @param connectTimeout the connectTimeout to set
	 */
	public void setConnectTimeout(int connectTimeout) {
		this.connectTimeout = connectTimeout;
	}

	/**
	 * @return the defaultTimeout
	 */
	public int getDefaultTimeout() {
		return defaultTimeout;
	}

	/**
	 * @param defaultTimeout the defaultTimeout to set
	 */
	public void setDefaultTimeout(int defaultTimeout) {
		this.defaultTimeout = defaultTimeout;
	}

	/**
	 * @param email email
	 * @throws EmailException if an error occurs
	 */
	public abstract void send(Email email) throws EmailException;
}
