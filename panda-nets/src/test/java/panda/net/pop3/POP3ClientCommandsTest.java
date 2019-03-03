package panda.net.pop3;

import java.io.IOException;
import java.io.Reader;
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
public class POP3ClientCommandsTest {
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

	@Test
	public void testNoopCommand() throws Exception {
		reset();
		connect();

		// Should fail before authorization
		Assert.assertFalse(p.noop());

		// Should pass in transaction state
		login();
		Assert.assertTrue(p.noop());

		// Should fail in update state
		p.setState(POP3.UPDATE_STATE);
		Assert.assertFalse(p.noop());
	}

	@Test
	public void testStatus() throws Exception {
		reset();
		connect();

		// Should fail in authorization state
		Assert.assertNull(p.status());

		// Should pass on a mailbox with mail in it
		login();
		POP3MessageInfo msg = p.status();
		Assert.assertTrue(msg.number > 0);
		Assert.assertTrue(msg.size > 0);
		Assert.assertNull(msg.identifier);
		p.logout();

		// Should also pass on a mailbox with no mail in it
		reset();
		connect();
		Assert.assertTrue(p.login(emptyUser, password));
		POP3MessageInfo msg2 = p.status();
		Assert.assertEquals(0, msg2.number);
		Assert.assertEquals(0, msg2.size);
		Assert.assertNull(msg2.identifier);
		p.logout();

		// Should fail in the 'update' state
		reset();
		connect();
		login();
		p.setState(POP3.UPDATE_STATE);
		Assert.assertNull(p.status());
	}

	@Test
	public void testListMessagesOnFullMailbox() throws Exception {
		reset();
		connect();
		login();

		POP3MessageInfo[] msg = p.listMessages();
		Assert.assertTrue(msg.length > 0);

		for (int i = 0; i < msg.length; i++) {
			Assert.assertNotNull(msg[i]);
			Assert.assertEquals(i + 1, msg[i].number);
			Assert.assertTrue(msg[i].size > 0);
			Assert.assertNull(msg[i].identifier);
		}

		// Now test from the update state
		p.setState(POP3.UPDATE_STATE);
		msg = p.listMessages();
		Assert.assertNull(msg);
	}

	@Test
	public void testListMessageOnFullMailbox() throws Exception {
		reset();
		connect();
		login();

		// The first message is always at index 1
		POP3MessageInfo msg = p.listMessage(1);
		Assert.assertNotNull(msg);
		Assert.assertEquals(1, msg.number);
		Assert.assertTrue(msg.size > 0);
		Assert.assertNull(msg.identifier);

		// Now retrieve a message from index 0
		msg = p.listMessage(0);
		Assert.assertNull(msg);

		// Now retrieve a msg that is not there
		msg = p.listMessage(100000);
		Assert.assertNull(msg);

		// Now retrieve a msg with a negative index
		msg = p.listMessage(-2);
		Assert.assertNull(msg);

		// Now try to get a valid message from the update state
		p.setState(POP3.UPDATE_STATE);
		msg = p.listMessage(1);
		Assert.assertNull(msg);
	}

	@Test
	public void testListMessagesOnEmptyMailbox() throws Exception {
		reset();
		connect();
		Assert.assertTrue(p.login(emptyUser, password));

		POP3MessageInfo[] msg = p.listMessages();
		Assert.assertEquals(0, msg.length);

		// Now test from the update state
		p.setState(POP3.UPDATE_STATE);
		msg = p.listMessages();
		Assert.assertNull(msg);
	}

	@Test
	public void testListMessageOnEmptyMailbox() throws Exception {
		reset();
		connect();
		Assert.assertTrue(p.login(emptyUser, password));

		// The first message is always at index 1
		POP3MessageInfo msg = p.listMessage(1);
		Assert.assertNull(msg);
	}

	@Test
	public void testListUniqueIDsOnFullMailbox() throws Exception {
		reset();
		connect();
		login();

		POP3MessageInfo[] msg = p.listUniqueIdentifiers();
		Assert.assertTrue(msg.length > 0);

		for (int i = 0; i < msg.length; i++) {
			Assert.assertNotNull(msg[i]);
			Assert.assertEquals(i + 1, msg[i].number);
			Assert.assertNotNull(msg[i].identifier);
		}

		// Now test from the update state
		p.setState(POP3.UPDATE_STATE);
		msg = p.listUniqueIdentifiers();
		Assert.assertNull(msg);
	}

	@Test
	public void testListUniqueIDOnFullMailbox() throws Exception {
		reset();
		connect();
		login();

		// The first message is always at index 1
		POP3MessageInfo msg = p.listUniqueIdentifier(1);
		Assert.assertNotNull(msg);
		Assert.assertEquals(1, msg.number);
		Assert.assertNotNull(msg.identifier);

		// Now retrieve a message from index 0
		msg = p.listUniqueIdentifier(0);
		Assert.assertNull(msg);

		// Now retrieve a msg that is not there
		msg = p.listUniqueIdentifier(100000);
		Assert.assertNull(msg);

		// Now retrieve a msg with a negative index
		msg = p.listUniqueIdentifier(-2);
		Assert.assertNull(msg);

		// Now try to get a valid message from the update state
		p.setState(POP3.UPDATE_STATE);
		msg = p.listUniqueIdentifier(1);
		Assert.assertNull(msg);
	}

	@Test
	public void testListUniqueIDsOnEmptyMailbox() throws Exception {
		reset();
		connect();
		Assert.assertTrue(p.login(emptyUser, password));

		POP3MessageInfo[] msg = p.listUniqueIdentifiers();
		Assert.assertEquals(0, msg.length);

		// Now test from the update state
		p.setState(POP3.UPDATE_STATE);
		msg = p.listUniqueIdentifiers();
		Assert.assertNull(msg);
	}

	@Test
	public void testListUniqueIdentifierOnEmptyMailbox() throws Exception {
		reset();
		connect();
		Assert.assertTrue(p.login(emptyUser, password));

		// The first message is always at index 1
		POP3MessageInfo msg = p.listUniqueIdentifier(1);
		Assert.assertNull(msg);
	}

	@Test
	public void testRetrieveMessageOnFullMailbox() throws Exception {
		reset();
		connect();
		login();
		int reportedSize = 0;
		int actualSize = 0;

		POP3MessageInfo[] msg = p.listMessages();
		Assert.assertTrue(msg.length > 0);

		for (int i = msg.length; i > 0; i--) {
			reportedSize = msg[i - 1].size;
			Reader r = p.retrieveMessage(i);
			Assert.assertNotNull(r);

			int delaycount = 0;
			if (!r.ready()) {
				// Give the reader time to get the message
				// from the server
				Thread.sleep(500);
				delaycount++;
				// but don't wait too long
				if (delaycount == 4) {
					break;
				}
			}
			while (r.ready()) {
				r.read();
				actualSize++;
			}
			// Due to variations in line termination
			// on different platforms, the actual
			// size may vary slightly. On Win2KPro, the
			// actual size is 2 bytes larger than the reported
			// size.
			Assert.assertTrue(actualSize >= reportedSize);
		}
	}

	@Test
	public void testRetrieveMessageOnEmptyMailbox() throws Exception {
		reset();
		connect();
		Assert.assertTrue(p.login(emptyUser, password));
		Assert.assertNull(p.retrieveMessage(1));
	}

	@Test
	public void testRetrieveMessageShouldFails() throws Exception {
		reset();
		connect();
		login();

		// Try to get message 0
		Assert.assertNull(p.retrieveMessage(0));

		// Try to get a negative message
		Assert.assertNull(p.retrieveMessage(-2));

		// Try to get a message that is not there
		Assert.assertNull(p.retrieveMessage(100000));

		// Change states and try to get a valid message
		p.setState(POP3.UPDATE_STATE);
		Assert.assertNull(p.retrieveMessage(1));
	}

	@Test
	public void testRetrieveMessageTopOnFullMailbox() throws Exception {
		reset();
		connect();
		login();
		int numLines = 10;

		POP3MessageInfo[] msg = p.listMessages();
		Assert.assertTrue(msg.length > 0);

		for (int i = 0; i < msg.length; i++) {
			Reader r = p.retrieveMessageTop(i + 1, numLines);
			Assert.assertNotNull(r);
			r.close();
			r = null;
		}
	}

	@Test
	public void testRetrieveOverSizedMessageTopOnFullMailbox() throws Exception {
		reset();
		connect();
		login();
		int reportedSize = 0;
		int actualSize = 0;

		POP3MessageInfo msg = p.listMessage(1);
		reportedSize = msg.size;

		// Now try to retrieve more lines than exist in the message
		Reader r = p.retrieveMessageTop(1, 100000);
		Assert.assertNotNull(r);

		int delaycount = 0;
		while (!r.ready()) {
			// Give the reader time to get the message
			// from the server
			Thread.sleep(500);
			delaycount++;
			// but don't wait too long
			if (delaycount == 4) {
				break;
			}
		}
		while (r.ready()) {
			r.read();
			actualSize++;
		}
		// Due to variations in line termination
		// on different platforms, the actual
		// size may vary slightly. On Win2KPro, the
		// actual size is 2 bytes larger than the reported
		// size.
		Assert.assertTrue(actualSize >= reportedSize);
	}

	@Test
	public void testRetrieveMessageTopOnEmptyMailbox() throws Exception {
		reset();
		connect();
		Assert.assertTrue(p.login(emptyUser, password));
		Assert.assertNull(p.retrieveMessageTop(1, 10));
	}

	@Test
	public void testRetrieveMessageTopShouldFails() throws Exception {
		reset();
		connect();
		login();

		// Try to get message 0
		Assert.assertNull(p.retrieveMessageTop(0, 10));

		// Try to get a negative message
		Assert.assertNull(p.retrieveMessageTop(-2, 10));

		// Try to get a message that is not there
		Assert.assertNull(p.retrieveMessageTop(100000, 10));

		// Change states and try to get a valid message
		p.setState(POP3.UPDATE_STATE);
		Assert.assertNull(p.retrieveMessageTop(1, 10));
	}

	@Test
	public void testDeleteWithReset() throws Exception {
		reset();
		connect();
		login();
		// Get the original number of messages
		POP3MessageInfo[] msg = p.listMessages();
		int numMessages = msg.length;
		int numDeleted = 0;

		// Now delete some and logout
		for (int i = 0; i < numMessages - 1; i++) {
			p.deleteMessage(i + 1);
			numDeleted++;
		}
		// Check to see that they are marked as deleted
		Assert.assertEquals(numMessages, (numDeleted + 1));

		// Now reset to unmark the messages as deleted
		p.reset();

		// Logout and come back in
		p.logout();
		reset();
		connect();
		login();

		// Get the new number of messages, because of
		// reset, new number should match old number
		msg = p.listMessages();
		Assert.assertEquals(numMessages, msg.length);
	}

	@Test
	public void testDelete() throws Exception {
		reset();
		connect();
		login();
		// Get the original number of messages
		POP3MessageInfo[] msg = p.listMessages();
		int numMessages = msg.length;
		int numDeleted = 0;

		// Now delete some and logout
		for (int i = 0; i < numMessages - 3; i++) {
			p.deleteMessage(i + 1);
			numDeleted++;
		}
		// Check to see that they are marked as deleted
		Assert.assertEquals(numMessages, (numDeleted + 3));

		// Logout and come back in
		p.logout();
		reset();
		connect();
		login();

		// Get the new number of messages, because of
		// reset, new number should match old number
		msg = p.listMessages();
		Assert.assertEquals(numMessages - numDeleted, msg.length);
	}

	@Test
	public void testResetAndDeleteShouldFails() throws Exception {
		reset();
		connect();
		login();

		p.setState(POP3.UPDATE_STATE);
		Assert.assertFalse(p.reset());

		Assert.assertFalse(p.deleteMessage(1));
	}
}
