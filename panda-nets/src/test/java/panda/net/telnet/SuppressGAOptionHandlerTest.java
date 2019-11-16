package panda.net.telnet;

import panda.net.telnet.SuppressGAOptionHandler;
import panda.net.telnet.TelnetCommand;
import panda.net.telnet.TelnetOption;

/***
 * JUnit test class for SuppressGAOptionHandler
 ***/
public class SuppressGAOptionHandlerTest extends TelnetOptionHandlerTestAbstract {

	/***
	 * setUp for the test.
	 ***/
	@Override
	protected void setUp() {
		opthand1 = new SuppressGAOptionHandler();
		opthand2 = new SuppressGAOptionHandler(true, true, true, true);
		opthand3 = new SuppressGAOptionHandler(false, false, false, false);
	}

	/***
	 * test of the constructors.
	 ***/
	@Override
	public void testConstructors() {
		assertEquals(opthand1.getOptionCode(), TelnetOption.SUPPRESS_GO_AHEAD);
		super.testConstructors();
	}

	/***
	 * test of client-driven subnegotiation. Checks that no subnegotiation is made.
	 ***/
	@Override
	public void testStartSubnegotiation() {

		int resp1[] = opthand1.startSubnegotiationLocal();
		int resp2[] = opthand1.startSubnegotiationRemote();

		assertEquals(resp1, null);
		assertEquals(resp2, null);
	}

	/***
	 * test of server-driven subnegotiation. Checks that no subnegotiation is made.
	 ***/
	@Override
	public void testAnswerSubnegotiation() {
		int subn[] = { TelnetCommand.IAC, TelnetCommand.SB, TelnetOption.SUPPRESS_GO_AHEAD, 1, TelnetCommand.IAC,
				TelnetCommand.SE, };

		int resp1[] = opthand1.answerSubnegotiation(subn, subn.length);

		assertEquals(resp1, null);
	}
}
