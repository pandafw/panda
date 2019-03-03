package panda.net.pop3;

import java.io.Reader;

import org.junit.Assert;
import org.junit.Assume;
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
public class POP3ConstructorTest {
	String user = POP3Constants.user;
	String emptyUser = POP3Constants.emptyuser;
	String password = POP3Constants.password;
	String mailhost = POP3Constants.getMailhost();

	/*
	 * This test will ensure that the constants are not inadvertently changed. If the constants are
	 * changed in panda.net.pop3 for some reason, this test will have to be updated.
	 */
	@Test
	public void testConstants() {
		// From POP3
		Assert.assertEquals(110, POP3.DEFAULT_PORT);
		Assert.assertEquals(-1, POP3.DISCONNECTED_STATE);
		Assert.assertEquals(0, POP3.AUTHORIZATION_STATE);
		Assert.assertEquals(1, POP3.TRANSACTION_STATE);
		Assert.assertEquals(2, POP3.UPDATE_STATE);

		// From POP3Command
		Assert.assertEquals(0, POP3Command.USER);
		Assert.assertEquals(1, POP3Command.PASS);
		Assert.assertEquals(2, POP3Command.QUIT);
		Assert.assertEquals(3, POP3Command.STAT);
		Assert.assertEquals(4, POP3Command.LIST);
		Assert.assertEquals(5, POP3Command.RETR);
		Assert.assertEquals(6, POP3Command.DELE);
		Assert.assertEquals(7, POP3Command.NOOP);
		Assert.assertEquals(8, POP3Command.RSET);
		Assert.assertEquals(9, POP3Command.APOP);
		Assert.assertEquals(10, POP3Command.TOP);
		Assert.assertEquals(11, POP3Command.UIDL);
	}

	@Test
	public void testPOP3DefaultConstructor() {
		POP3 pop = new POP3();

		Assert.assertEquals(110, pop.getDefaultPort());
		Assert.assertEquals(POP3.DISCONNECTED_STATE, pop.getState());
		Assert.assertNull(pop._reader);
		Assert.assertNotNull(pop._replyLines);
	}

	@Test
	public void testPOP3ClientStateTransition() throws Exception {
		POP3Client pop = new POP3Client();

		// Initial state
		Assert.assertEquals(110, pop.getDefaultPort());
		Assert.assertEquals(POP3.DISCONNECTED_STATE, pop.getState());
		Assert.assertNull(pop._reader);
		Assert.assertNotNull(pop._replyLines);

		// Now connect
		if (Strings.isEmpty(mailhost)) {
			Assume.assumeTrue(false);
		}

		pop.connect(mailhost);
		Assert.assertEquals(POP3.AUTHORIZATION_STATE, pop.getState());

		// Now authenticate
		pop.login(user, password);
		Assert.assertEquals(POP3.TRANSACTION_STATE, pop.getState());

		// Now do a series of commands and make sure the state stays as it should
		pop.noop();
		Assert.assertEquals(POP3.TRANSACTION_STATE, pop.getState());
		pop.status();
		Assert.assertEquals(POP3.TRANSACTION_STATE, pop.getState());

		// Make sure we have at least one message to test
		POP3MessageInfo[] msg = pop.listMessages();

		if (msg.length > 0) {
			pop.deleteMessage(1);
			Assert.assertEquals(POP3.TRANSACTION_STATE, pop.getState());

			pop.reset();
			Assert.assertEquals(POP3.TRANSACTION_STATE, pop.getState());

			pop.listMessage(1);
			Assert.assertEquals(POP3.TRANSACTION_STATE, pop.getState());

			pop.listMessages();
			Assert.assertEquals(POP3.TRANSACTION_STATE, pop.getState());

			pop.listUniqueIdentifier(1);
			Assert.assertEquals(POP3.TRANSACTION_STATE, pop.getState());

			pop.listUniqueIdentifiers();
			Assert.assertEquals(POP3.TRANSACTION_STATE, pop.getState());

			Reader r = pop.retrieveMessage(1);
			Assert.assertEquals(POP3.TRANSACTION_STATE, pop.getState());

			// Add some sleep here to handle network latency
			while (!r.ready()) {
				Thread.sleep(10);
			}
			r.close();
			r = null;

			r = pop.retrieveMessageTop(1, 10);
			Assert.assertEquals(POP3.TRANSACTION_STATE, pop.getState());

			// Add some sleep here to handle network latency
			while (!r.ready()) {
				Thread.sleep(10);
			}
			r.close();
			r = null;

		}

		// Now logout
		pop.logout();
		Assert.assertEquals(POP3.UPDATE_STATE, pop.getState());
	}
}
