package panda.net.telnet;

/***
 * The InvalidTelnetOptionException is the exception that is thrown whenever a TelnetOptionHandler
 * with an invlaid option code is registered in TelnetClient with addOptionHandler.
 ***/
public class InvalidTelnetOptionException extends Exception {

	private static final long serialVersionUID = -2516777155928793597L;

	/***
	 * Option code
	 ***/
	private final int optionCode;

	/***
	 * Error message
	 ***/
	private final String msg;

	/***
	 * Constructor for the exception.
	 * <p>
	 * 
	 * @param message - Error message.
	 * @param optcode - Option code.
	 ***/
	public InvalidTelnetOptionException(String message, int optcode) {
		optionCode = optcode;
		msg = message;
	}

	/***
	 * Gets the error message of ths exception.
	 * <p>
	 * 
	 * @return the error message.
	 ***/
	@Override
	public String getMessage() {
		return (msg + ": " + optionCode);
	}
}
