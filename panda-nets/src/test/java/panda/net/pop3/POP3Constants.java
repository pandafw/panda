package panda.net.pop3;

/**
 * The POP3* tests all presume the existence of the following parameters: mailserver: localhost
 * (running on the default port 110) account: username=test; password=password account:
 * username=alwaysempty; password=password. mail: At least four emails in the test account and zero
 * emails in the alwaysempty account If this won't work for you, you can change these parameters in
 * the TestSetupParameters class. The tests were originally run on a default installation of James.
 * Your mileage may vary based on the POP3 server you run the tests against. Some servers are more
 * standards-compliant than others.
 */
public class POP3Constants {
	public static final String user = "test";
	public static final String emptyuser = "alwaysempty";
	public static final String password = "password";

	// Cannot be instantiated
	private POP3Constants() {
	}
	
	public static String getMailhost() {
		return System.getenv("PANDA.NET.POP3.HOST");
	}
}
