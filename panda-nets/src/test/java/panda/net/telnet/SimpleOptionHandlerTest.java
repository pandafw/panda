package panda.net.telnet;

import panda.net.telnet.SimpleOptionHandler;
import panda.net.telnet.TelnetCommand;

/***
 * JUnit test class for SimpleOptionHandler
 ***/
public class SimpleOptionHandlerTest extends TelnetOptionHandlerTestAbstract {
	/***
	 * setUp for the test.
	 ***/
	@Override
	protected void setUp() {
		opthand1 = new SimpleOptionHandler(4);
		opthand2 = new SimpleOptionHandler(8, true, true, true, true);
		opthand3 = new SimpleOptionHandler(91, false, false, false, false);
	}

	/***
	 * test of the constructors.
	 ***/
	@Override
	public void testConstructors() {
		assertEquals(opthand1.getOptionCode(), 4);
		assertEquals(opthand2.getOptionCode(), 8);
		assertEquals(opthand3.getOptionCode(), 91);
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
		int subn[] = { TelnetCommand.IAC, TelnetCommand.SB, 4, 1, TelnetCommand.IAC, TelnetCommand.SE, };

		int resp1[] = opthand1.answerSubnegotiation(subn, subn.length);

		assertEquals(resp1, null);
	}
}
