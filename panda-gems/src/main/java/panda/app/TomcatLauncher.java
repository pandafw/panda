package panda.app;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import javax.naming.CompositeName;
import javax.naming.StringRefAddr;
import javax.servlet.annotation.ServletSecurity.TransportGuarantee;

import org.apache.catalina.Context;
import org.apache.catalina.Globals;
import org.apache.catalina.Host;
import org.apache.catalina.LifecycleEvent;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.LifecycleListener;
import org.apache.catalina.LifecycleState;
import org.apache.catalina.Role;
import org.apache.catalina.Server;
import org.apache.catalina.connector.Connector;
import org.apache.catalina.core.StandardContext;
import org.apache.catalina.core.StandardServer;
import org.apache.catalina.startup.ExpandWar;
import org.apache.catalina.startup.Tomcat;
import org.apache.catalina.users.MemoryUserDatabase;
import org.apache.catalina.users.MemoryUserDatabaseFactory;
import org.apache.coyote.AbstractProtocol;
import org.apache.coyote.ProtocolHandler;
import org.apache.tomcat.util.descriptor.web.LoginConfig;
import org.apache.tomcat.util.descriptor.web.SecurityCollection;
import org.apache.tomcat.util.descriptor.web.SecurityConstraint;
import org.apache.tomcat.util.scan.StandardJarScanner;

import panda.args.Argument;
import panda.args.CmdLineException;
import panda.args.CmdLineParser;
import panda.args.Option;
import panda.lang.Strings;

import webapp.runner.launch.valves.StdoutAccessLogValve;

public class TomcatLauncher {

	private static final String AUTH_ROLE = "user";

	public static class CommandLineArgs {
		@Option(opt='h', option="help", usage="print usage")
		public boolean help;

		@Option(option="session-timeout", arg="TIMEOUT", usage="The number of minutes of inactivity before a user's session is timed out.")
		public Integer sessionTimeout;

		@Option(option="port", arg="PORT", usage="The port that the server will accept http requests on.")
		public Integer port = 8080;

		@Option(option="context-xml", arg="XML", usage="The path to the context xml to use.")
		public String contextXml;

		@Option(option="path", arg="PATH", usage="The context path")
		public String contextPath = "";

		@Option(option="shutdown-override", usage="Overrides the default behavior and casues Tomcat to ignore lifecycle failure events rather than shutting down when they occur.")
		public boolean shutdownOverride;

		@Option(option="enable-compression", usage="Enable GZIP compression on responses")
		public boolean enableCompression;

		@Option(option="compressable-mime-types", arg="TYPES", usage="Comma delimited list of mime types that will be compressed when using GZIP compression.")
		public String compressableMimeTypes = "text/html,text/xml,text/plain,text/css,application/json,application/xml,text/javascript,application/javascript";

		@Option(option="enable-ssl", usage="Specify -Djavax.net.ssl.keyStore, -Djavax.net.ssl.keystoreStorePassword, -Djavax.net.ssl.trustStore and -Djavax.net.ssl.trustStorePassword in JAVA_OPTS. Note: should not be used if a reverse proxy is terminating SSL for you (such as on Heroku)")
		public boolean enableSSL;

		@Option(option="enable-client-auth", usage="Specify -Djavax.net.ssl.keyStore and -Djavax.net.ssl.keyStorePassword in JAVA_OPTS")
		public boolean enableClientAuth;

		@Option(option="enable-basic-auth", usage="Secure the app with basic auth. Use with --basic-auth-user and --basic-auth-pw or --tomcat-users-location")
		public boolean enableBasicAuth;

		@Option(option="basic-auth-user", arg="AUTH", usage="Username to be used with basic auth. Defaults to BASIC_AUTH_USER env variable.")
		public String basicAuthUser;

		@Option(option="basic-auth-pw", arg="PWD", usage="Password to be used with basic auth. Defaults to BASIC_AUTH_PW env variable.")
		public String basicAuthPw;

		@Option(option="tomcat-users-location", arg="LOCATION", usage="Location of the tomcat-users.xml file. (relative to the location of the webapp-runner jar file)")
		public String tomcatUsersLocation;

		@Option(option="uri-encoding", arg="ENCODING", usage="Set the URI encoding to be used for the Connector.")
		public String uriEncoding;

		@Option(option="use-body-encoding-for-uri", usage="Set if the entity body encoding should be used for the URI.")
		public boolean useBodyEncodingForURI;

		@Option(option="scanBootstrapClassPath", usage="Set jar scanner scan bootstrap classpath.")
		public boolean scanBootstrapClassPath;

		@Option(option="temp-directory", arg="DIR", usage="Define the temp directory, default value: ~/tomcat")
		public String tempDirectory = null;

		@Option(option="bind-on-start", usage="Controls when the socket used by the connector is bound. By default it is bound when the connector is initiated and unbound when the connector is destroyed. If set to true, the socket will be bound when the connector is started and unbound when it is stopped.")
		public boolean bindOnStart;

		@Option(option="proxy-base-url", usage="Set proxy URL if tomcat is running behind reverse proxy")
		public String proxyBaseUrl = "";

		@Option(option="max-threads", usage="Set the maximum number of worker threads")
		public Integer maxThreads = 0;

		public Map<String, String> attributes = new HashMap<String, String>();

		@Option(opt='A', option="attr", usage="Allows setting HTTP connector attributes. For example: -Acompression=on")
		public void setAttribute(String attr) {
			String[] as = Strings.split(attr, '-');
			if (as.length != 2) {
				throw new IllegalArgumentException("Invalid option -A" + attr);
			}
			attributes.put(Strings.strip(as[0]), Strings.strip(as[1]));
		}
		
		@Option(option="enable-naming", usage="Enables JNDI naming")
		public boolean enableNaming;

		@Option(option="access-log", usage="Enables AccessLogValue to STDOUT")
		public boolean accessLog;

		@Option(option="access-log-pattern", arg="PATTERN", usage="If --access-log is enabled, sets the logging pattern")
		public String accessLogPattern = "common";

		@Option(option="sslport", arg="PORT", usage="The SSL port that the server will accept http requests on.")
		public Integer sslport = 0;

		@Option(option="keystoreFile", arg="PATH", usage="The keystore file.")
		public String keystoreFile;

		@Option(option="keystorePass", arg="PASSWORD", usage="The keystore password.")
		public String keystorePass;

		@Option(option="keystoreType", arg="TYPE", usage="The keystore type.")
		public String keystoreType;
		
		@Option(option="relaxedPathChars", arg="CHARS", usage="relaxedPathChars.")
		public String relaxedPathChars;

		@Option(option="relaxedQueryChars", arg="CHARS", usage="relaxedQueryChars.")
		public String relaxedQueryChars;

		@Argument(name="WAR", index=0, usage="The war path.")
		public String war = "web";
	}
	
	public static void main(String[] args) throws Exception {
		CommandLineArgs clas = new CommandLineArgs();

		CmdLineParser clp = new CmdLineParser(clas);
		try {
			clp.parse(args);

			if (clas.help) {
				System.out.print(clp.usage());
				return;
			}

			clp.validate();
		}
		catch (CmdLineException e) {
			System.out.println("ERROR: " + e.getMessage());
			System.out.println();
			System.out.println(clp.usage());
			return;
		}

		final Tomcat tomcat = new Tomcat();

		// set directory for temp files
		tomcat.setBaseDir(resolveTomcatBaseDir(clas.port, clas.tempDirectory));

		// initialize the connector
		Connector nioConnector = new Connector("org.apache.coyote.http11.Http11NioProtocol");
		nioConnector.setPort(clas.port);

		Connector snioConnector = null;
		if (clas.sslport > 0) {
			snioConnector = new Connector("org.apache.coyote.http11.Http11NioProtocol");
			snioConnector.setPort(clas.sslport);
			snioConnector.setSecure(true);
			snioConnector.setProperty("SSLEnabled", "true");
			snioConnector.setProperty("allowUnsafeLegacyRenegotiation", "false");

			String pathToTrustStore = System.getProperty("javax.net.ssl.trustStore");
			if (pathToTrustStore != null) {
				snioConnector.setProperty("sslProtocol", "tls");
				File truststoreFile = new File(pathToTrustStore);
				snioConnector.setAttribute("truststoreFile", truststoreFile.getAbsolutePath());
				System.out.println(truststoreFile.getAbsolutePath());
				snioConnector.setAttribute("trustStorePassword", System.getProperty("javax.net.ssl.trustStorePassword"));
			}

			String pathToKeystore = clas.keystoreFile;
			if (pathToKeystore != null) {
				File keystoreFile = new File(pathToKeystore);
				snioConnector.setAttribute("keystoreFile", keystoreFile.getAbsolutePath());
				System.out.println(keystoreFile.getAbsolutePath());
				snioConnector.setAttribute("keystorePass", clas.keystorePass);
				if (Strings.isNotEmpty(clas.keystoreType)) {
					snioConnector.setAttribute("keystoreType", clas.keystoreType);
				}
			}
			if (clas.enableClientAuth) {
				snioConnector.setAttribute("clientAuth", true);
			}
		}

		if (clas.maxThreads > 0) {
			ProtocolHandler handler = nioConnector.getProtocolHandler();
			if (handler instanceof AbstractProtocol) {
				AbstractProtocol protocol = (AbstractProtocol)handler;
				protocol.setMaxThreads(clas.maxThreads);
			}
			else {
				System.out.println("WARNING: Could not set maxThreads!");
			}
		}

		if (nioConnector != null) {
			setConnectorProperties(nioConnector, clas);
			tomcat.setConnector(nioConnector);
			tomcat.setPort(clas.port);
		}
		if (snioConnector != null) {
			setConnectorProperties(snioConnector, clas);
			tomcat.getService().addConnector(snioConnector);
		}

		File war = new File(clas.war);
		if (!war.exists()) {
			System.err.println("The specified path \"" + clas.war + "\" does not exist.");
			System.exit(1);
		}

		Context ctx;

		final String ctxName = clas.contextPath;
		if (war.isFile()) {
			File appBase = new File(System.getProperty(Globals.CATALINA_BASE_PROP), tomcat.getHost().getAppBase());
			if (appBase.exists()) {
				appBase.delete();
			}
			appBase.mkdir();
			URL fileUrl = new URL("jar:" + war.toURI().toURL() + "!/");
			String expandedDir = ExpandWar.expand(tomcat.getHost(), fileUrl, "/expanded");
			System.out.println("Expanding " + war.getName() + " into " + expandedDir);

			System.out.println("Adding Context " + ctxName + " for " + expandedDir);
			ctx = tomcat.addWebapp(ctxName, expandedDir);
		}
		else {
			System.out.println("Adding Context " + ctxName + " for " + war.getPath());
			ctx = tomcat.addWebapp(ctxName, war.getAbsolutePath());
		}

		// we'll do it ourselves (see above)
		((StandardContext)ctx).setUnpackWAR(false);

		if (!clas.shutdownOverride) {
			// allow Tomcat to shutdown if a context failure is detected
			ctx.addLifecycleListener(new LifecycleListener() {
				public void lifecycleEvent(LifecycleEvent event) {
					if (event.getLifecycle().getState() == LifecycleState.FAILED) {
						Server server = tomcat.getServer();
						if (server instanceof StandardServer) {
							System.err.println("SEVERE: Context [" + ctxName + "] failed in [" + event.getLifecycle().getClass().getName()
									+ "] lifecycle. Allowing Tomcat to shutdown.");
							((StandardServer)server).stopAwait();
						}
					}
				}
			});
		}

		if (clas.scanBootstrapClassPath) {
			StandardJarScanner scanner = new StandardJarScanner();
			scanner.setScanBootstrapClassPath(true);
			ctx.setJarScanner(scanner);
		}

		// set the context xml location if there is only one war
		if (clas.contextXml != null) {
			System.out.println("Using context config: " + clas.contextXml);
			ctx.setConfigFile(new File(clas.contextXml).toURI().toURL());
		}

		// set the session timeout
		if (clas.sessionTimeout != null) {
			ctx.setSessionTimeout(clas.sessionTimeout);
		}

		addShutdownHook(tomcat);

		if (clas.enableNaming || clas.enableBasicAuth || clas.tomcatUsersLocation != null) {
			tomcat.enableNaming();
		}

		if (clas.enableBasicAuth) {
			enableBasicAuth(ctx, clas.enableSSL);
		}

		if (clas.accessLog) {
			Host host = tomcat.getHost();
			StdoutAccessLogValve valve = new StdoutAccessLogValve();
			valve.setEnabled(true);
			valve.setPattern(clas.accessLogPattern);
			host.getPipeline().addValve(valve);
		}

		// start the server
		tomcat.start();

		/*
		 * NamingContextListener.lifecycleEvent(LifecycleEvent event) cannot initialize GlobalNamingContext for Tomcat until the Lifecycle.CONFIGURE_START_EVENT occurs, so this block must sit after
		 * the call to tomcat.start() and it requires tomcat.enableNaming() to be called much earlier in the code.
		 */
		if (clas.enableBasicAuth || clas.tomcatUsersLocation != null) {
			configureUserStore(tomcat, clas);
		}

		clas = null;

		tomcat.getServer().await();
	}

	static void setConnectorProperties(Connector nioConnector, CommandLineArgs commandLineParams) throws URISyntaxException {
		if (Strings.isNotEmpty(commandLineParams.relaxedPathChars)) {
			nioConnector.setProperty("relaxedPathChars", commandLineParams.relaxedPathChars);
		}
		if (Strings.isNotEmpty(commandLineParams.relaxedQueryChars)) {
			nioConnector.setProperty("relaxedQueryChars", commandLineParams.relaxedQueryChars);
		}
		
		if (commandLineParams.proxyBaseUrl.length() > 0) {
			URI uri = new URI(commandLineParams.proxyBaseUrl);
			String scheme = uri.getScheme();
			nioConnector.setScheme(scheme);
			if (scheme.equals("https") && !nioConnector.getSecure()) {
				nioConnector.setSecure(true);
			}
			if (uri.getPort() > 0) {
				nioConnector.setProxyPort(uri.getPort());
			}
			else if (scheme.equals("http")) {
				nioConnector.setProxyPort(80);
			}
			else if (scheme.equals("https")) {
				nioConnector.setProxyPort(443);
			}
		}

		if (null != commandLineParams.uriEncoding) {
			nioConnector.setURIEncoding(commandLineParams.uriEncoding);
		}
		nioConnector.setUseBodyEncodingForURI(commandLineParams.useBodyEncodingForURI);

		if (commandLineParams.enableCompression) {
			nioConnector.setProperty("compression", "on");
			nioConnector.setProperty("compressableMimeType", commandLineParams.compressableMimeTypes);
		}

		if (commandLineParams.bindOnStart) {
			nioConnector.setProperty("bindOnInit", "false");
		}

		if (!commandLineParams.attributes.isEmpty()) {
			System.out.println("Connector attributes");
			for (final Map.Entry<String, String> entry : commandLineParams.attributes.entrySet()) {
				final String key = entry.getKey();
				final String value = entry.getValue();
				System.out.println("property: " + key + " - " + value);
				nioConnector.setProperty(entry.getKey(), entry.getValue());
			}
		}
	}

	/**
	 * Gets or creates temporary Tomcat base directory within target dir
	 *
	 * @param port port of web process
	 * @return absolute dir path
	 * @throws IOException if dir fails to be created
	 */
	static String resolveTomcatBaseDir(Integer port, String tempDirectory) throws IOException {
		final File baseDir = tempDirectory != null ? new File(tempDirectory) : new File(System.getProperty("user.dir") + "/tomcat");

		if (!baseDir.isDirectory() && !baseDir.mkdirs()) {
			throw new IOException("Could not create temp dir: " + baseDir);
		}

		try {
			return baseDir.getCanonicalPath();
		}
		catch (IOException e) {
			return baseDir.getAbsolutePath();
		}
	}

	/*
	 * Set up basic auth security on the entire application
	 */
	static void enableBasicAuth(Context ctx, boolean enableSSL) {
		LoginConfig loginConfig = new LoginConfig();
		loginConfig.setAuthMethod("BASIC");
		ctx.setLoginConfig(loginConfig);
		ctx.addSecurityRole(AUTH_ROLE);

		SecurityConstraint securityConstraint = new SecurityConstraint();
		securityConstraint.addAuthRole(AUTH_ROLE);
		if (enableSSL) {
			securityConstraint.setUserConstraint(TransportGuarantee.CONFIDENTIAL.toString());
		}
		SecurityCollection securityCollection = new SecurityCollection();
		securityCollection.addPattern("/*");
		securityConstraint.addCollection(securityCollection);
		ctx.addConstraint(securityConstraint);
	}

	static void configureUserStore(final Tomcat tomcat, final CommandLineArgs commandLineParams) throws Exception {
		String tomcatUsersLocation = commandLineParams.tomcatUsersLocation;
		if (tomcatUsersLocation == null) {
			tomcatUsersLocation = "../../tomcat-users.xml";
		}

		javax.naming.Reference ref = new javax.naming.Reference("org.apache.catalina.UserDatabase");
		ref.add(new StringRefAddr("pathname", tomcatUsersLocation));
		MemoryUserDatabase memoryUserDatabase =
				(MemoryUserDatabase)new MemoryUserDatabaseFactory().getObjectInstance(ref, new CompositeName("UserDatabase"), null, null);

		// Add basic auth user
		if (commandLineParams.basicAuthUser != null && commandLineParams.basicAuthPw != null) {

			memoryUserDatabase.setReadonly(false);
			Role user = memoryUserDatabase.createRole(AUTH_ROLE, AUTH_ROLE);
			memoryUserDatabase.createUser(commandLineParams.basicAuthUser, commandLineParams.basicAuthPw, commandLineParams.basicAuthUser).addRole(user);
			memoryUserDatabase.save();

		}
		else if (System.getenv("BASIC_AUTH_USER") != null && System.getenv("BASIC_AUTH_PW") != null) {

			memoryUserDatabase.setReadonly(false);
			Role user = memoryUserDatabase.createRole(AUTH_ROLE, AUTH_ROLE);
			memoryUserDatabase.createUser(System.getenv("BASIC_AUTH_USER"), System.getenv("BASIC_AUTH_PW"), System.getenv("BASIC_AUTH_USER")).addRole(user);
			memoryUserDatabase.save();
		}

		// Register memoryUserDatabase with GlobalNamingContext
		System.out.println("MemoryUserDatabase: " + memoryUserDatabase);
		tomcat.getServer().getGlobalNamingContext().addToEnvironment("UserDatabase", memoryUserDatabase);

		org.apache.tomcat.util.descriptor.web.ContextResource ctxRes = new org.apache.tomcat.util.descriptor.web.ContextResource();
		ctxRes.setName("UserDatabase");
		ctxRes.setAuth("Container");
		ctxRes.setType("org.apache.catalina.UserDatabase");
		ctxRes.setDescription("User database that can be updated and saved");
		ctxRes.setProperty("factory", "org.apache.catalina.users.MemoryUserDatabaseFactory");
		ctxRes.setProperty("pathname", tomcatUsersLocation);
		tomcat.getServer().getGlobalNamingResources().addResource(ctxRes);
		tomcat.getEngine().setRealm(new org.apache.catalina.realm.UserDatabaseRealm());
	}

	/**
	 * Stops the embedded Tomcat server.
	 */
	static void addShutdownHook(final Tomcat tomcat) {

		// add shutdown hook to stop server
		Runtime.getRuntime().addShutdownHook(new Thread() {
			public void run() {
				try {
					if (tomcat != null) {
						tomcat.getServer().stop();
					}
				}
				catch (LifecycleException exception) {
					throw new RuntimeException("WARNING: Cannot Stop Tomcat " + exception.getMessage(), exception);
				}
			}
		});
	}
}
