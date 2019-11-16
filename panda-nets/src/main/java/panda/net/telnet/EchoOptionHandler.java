package panda.net.telnet;

/***
 * Implements the telnet echo option RFC 857.
 ***/
public class EchoOptionHandler extends TelnetOptionHandler {
	/***
	 * Constructor for the EchoOptionHandler. Allows defining desired initial setting for
	 * local/remote activation of this option and behaviour in case a local/remote activation
	 * request for this option is received.
	 * <p>
	 * 
	 * @param initlocal - if set to true, a WILL is sent upon connection.
	 * @param initremote - if set to true, a DO is sent upon connection.
	 * @param acceptlocal - if set to true, any DO request is accepted.
	 * @param acceptremote - if set to true, any WILL request is accepted.
	 ***/
	public EchoOptionHandler(boolean initlocal, boolean initremote, boolean acceptlocal, boolean acceptremote) {
		super(TelnetOption.ECHO, initlocal, initremote, acceptlocal, acceptremote);
	}

	/***
	 * Constructor for the EchoOptionHandler. Initial and accept behaviour flags are set to false
	 ***/
	public EchoOptionHandler() {
		super(TelnetOption.ECHO, false, false, false, false);
	}

}
