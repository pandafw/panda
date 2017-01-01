package panda.net.telnet;

import panda.net.telnet.TelnetOption;

import junit.framework.TestCase;

/***
 * JUnit test class for TelnetOption
 ***/
public class TelnetOptionTest extends TestCase {
	/***
	 * test of the isValidOption method.
	 ***/
	public void testisValidOption() {
		assertTrue(TelnetOption.isValidOption(0));
		assertTrue(TelnetOption.isValidOption(91));
		assertTrue(TelnetOption.isValidOption(255));
		assertTrue(!TelnetOption.isValidOption(256));
	}

	/***
	 * test of the getOption method.
	 ***/
	public void testGetOption() {
		assertEquals(TelnetOption.getOption(0), "BINARY");
		assertEquals(TelnetOption.getOption(91), "UNASSIGNED");
		assertEquals(TelnetOption.getOption(255), "Extended-Options-List");
	}
}
