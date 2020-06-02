package panda.net.telnet;

/***
 * JUnit test class for EchoOptionHandler
 ***/
public class EchoOptionHandlerTest extends TelnetOptionHandlerTestAbstract {

	/***
	 * setUp for the test.
	 ***/
	@Override
	protected void setUp() {
		opthand1 = new EchoOptionHandler();
		opthand2 = new EchoOptionHandler(true, true, true, true);
		opthand3 = new EchoOptionHandler(false, false, false, false);
	}

	/***
	 * test of the constructors.
	 ***/
	@Override
	public void testConstructors() {
		assertEquals(opthand1.getOptionCode(), TelnetOption.ECHO);
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
		int subn[] = { TelnetCommand.IAC, TelnetCommand.SB, TelnetOption.ECHO, 1, TelnetCommand.IAC, TelnetCommand.SE, };

		int resp1[] = opthand1.answerSubnegotiation(subn, subn.length);

		assertEquals(resp1, null);
	}
}
