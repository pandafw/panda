package panda.net.telnet;

import junit.framework.TestCase;

/***
 * JUnit test class for InvalidTelnetOptionException
 ***/
public class InvalidTelnetOptionExceptionTest extends TestCase {
	private InvalidTelnetOptionException exc1;
	private String msg1;
	private int code1;

	/***
	 * setUp for the test.
	 ***/
	@Override
	protected void setUp() {
		msg1 = "MSG";
		code1 = 13;
		exc1 = new InvalidTelnetOptionException(msg1, code1);
	}

	/***
	 * test of the constructors.
	 ***/
	public void testConstructors() {
		assertTrue(exc1.getMessage().indexOf(msg1) >= 0);
		assertTrue(exc1.getMessage().indexOf("" + code1) >= 0);
	}
}
