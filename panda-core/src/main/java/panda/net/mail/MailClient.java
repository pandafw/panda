package panda.net.mail;

import panda.lang.Classes;
import panda.lang.Strings;
import panda.lang.Systems;
import panda.log.Log;
import panda.log.Logs;

/**
 * a interface for send mail
 */
public abstract class MailClient {
	private static Log LOG;

	public static MailClient create(String type) {
		if (Strings.isEmpty(type)) {
			if (Systems.IS_OS_APPENGINE) {
				return new JavaMailClient();
			}
			
			String cls = Strings.replace(JavaMailClient.class.getName(), "Java", "Smtp");
			try {
				return (MailClient)Classes.born(cls);
			}
			catch (Throwable e) {
				try {
					return new JavaMailClient();
				}
				catch (Throwable e2) {
					throw new RuntimeException("Failed to initialize "
						+ cls + " or " + JavaMailClient.class.getName() 
						+ " - please add panda-nets or javax.mail dependent jar.");
				}
			}
		}

		if (Strings.equalsIgnoreCase("java", type)) {
			return new JavaMailClient();
		}
		
		if (Strings.equalsIgnoreCase("smtp", type)) {
			return (MailClient)Classes.born(Strings.replace(JavaMailClient.class.getName(), "Java", "Smtp"));
		}
		
		return (MailClient)Classes.born(type);
	}
	
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
		if (debug) {
			if (log == null) {
				if (LOG == null) {
					// !IMPORTANT: panda.log.impl.SmtpLogAdapter use this class
					// do not use static final log instance, it will cause dead loop
					LOG = Logs.getLog(getClass());
				}
				log = LOG;
			}
		}
		else {
			log = null;
		}
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
