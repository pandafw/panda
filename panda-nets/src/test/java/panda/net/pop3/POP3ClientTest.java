package panda.net.pop3;

import java.io.IOException;
import java.net.InetAddress;

import org.junit.Assert;
import org.junit.Assume;
import org.junit.Before;
import org.junit.Test;

import panda.lang.Strings;

/**
 * The POP3* tests all presume the existence of the following parameters: mailserver: localhost
 * (running on the default port 110) account: username=test; password=password account:
 * username=alwaysempty; password=password. mail: At least four emails in the test account and zero
 * emails in the alwaysempty account If this won't work for you, you can change these parameters in
 * the TestSetupParameters class. The tests were originally run on a default installation of James.
 * Your mileage may vary based on the POP3 server you run the tests against. Some servers are more
 * standards-compliant than others.
 */
public class POP3ClientTest {
	POP3Client p = null;

	String user = POP3Constants.user;
	String emptyUser = POP3Constants.emptyuser;
	String password = POP3Constants.password;
	String mailhost = POP3Constants.getMailhost();

	@Before
	public void setUp() {
		if (Strings.isEmpty(mailhost)) {
			Assume.assumeTrue(false);
		}
	}

	private void reset() throws IOException {
		// Case where this is the first time reset is called
		if (p == null) {
			// Do nothing
		}
		else if (p.isConnected()) {
			p.disconnect();
		}
		p = null;
		p = new POP3Client();
	}

	private void connect() throws Exception {
		p.connect(InetAddress.getByName(mailhost));
		Assert.assertTrue(p.isConnected());
		Assert.assertEquals(POP3.AUTHORIZATION_STATE, p.getState());
	}

	private void login() throws Exception {
		Assert.assertTrue(p.login(user, password));
		Assert.assertEquals(POP3.TRANSACTION_STATE, p.getState());
	}

	/*
	 * Simple test to logon to a valid server using a valid user name and password.
	 */
	@Test
	public void testValidLoginWithNameAndPassword() throws Exception {
		reset();
		connect();

		// Try with a valid user
		login();
	}

	@Test
	public void testInvalidLoginWithBadName() throws Exception {
		reset();
		connect();

		// Try with an invalid user that doesn't exist
		Assert.assertFalse(p.login("badusername", password));
	}

	@Test
	public void testInvalidLoginWithBadPassword() throws Exception {
		reset();
		connect();

		// Try with a bad password
		Assert.assertFalse(p.login(user, "badpassword"));
	}

	/*
	 * Test to try to run the login method from the disconnected, transaction and update states
	 */
	@Test
	public void testLoginFromWrongState() throws Exception {
		reset();

		// Not currently connected, not in authorization state
		// Try to login with good name/password
		Assert.assertFalse(p.login(user, password));

		// Now connect and set the state to 'transaction' and try again
		connect();
		p.setState(POP3.TRANSACTION_STATE);
		Assert.assertFalse(p.login(user, password));
		p.disconnect();

		// Now connect and set the state to 'update' and try again
		connect();
		p.setState(POP3.UPDATE_STATE);
		Assert.assertFalse(p.login(user, password));
		p.disconnect();
	}

	@Test
	public void testLogoutFromAllStates() throws Exception {
		// From 'transaction' state
		reset();
		connect();
		login();
		Assert.assertTrue(p.logout());
		Assert.assertEquals(POP3.UPDATE_STATE, p.getState());

		// From 'update' state
		reset();
		connect();
		login();
		p.setState(POP3.UPDATE_STATE);
		Assert.assertTrue(p.logout());
	}
}
