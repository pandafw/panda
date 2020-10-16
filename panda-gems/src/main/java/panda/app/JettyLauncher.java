package panda.app;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.webapp.WebAppContext;

import panda.args.Argument;
import panda.args.CmdLineException;
import panda.args.CmdLineParser;
import panda.args.Option;
import panda.lang.Strings;

public class JettyLauncher {
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

		@Option(option="temp-directory", arg="DIR", usage="Define the temp directory, default value: ~/jetty")
		public String tempDirectory = null;

		@Option(option="bind-on-start", usage="Controls when the socket used by the connector is bound. By default it is bound when the connector is initiated and unbound when the connector is destroyed. If set to true, the socket will be bound when the connector is started and unbound when it is stopped.")
		public boolean bindOnStart;

		@Option(option="proxy-base-url", usage="Set proxy URL if tomcat is running behind reverse proxy")
		public String proxyBaseUrl = "";

		@Option(option="max-threads", usage="Set the maximum number of worker threads")
		public Integer maxThreads = 0;

		@Option(option="memcached-transcoder-factory-class", usage="The class name of the factory that creates the transcoder to use for serializing/deserializing sessions to/from memcached.")
		public String memcachedTranscoderFactoryClass = null;

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

		Server server = new Server(8080);

		WebAppContext webapp = new WebAppContext();
		webapp.setContextPath(clas.contextPath);
		webapp.setWar(clas.war);

		server.setHandler(webapp);
		server.setStopAtShutdown(true);

		server.start();
		server.join();
	}
}
