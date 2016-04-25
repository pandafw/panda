package panda.net;

import java.io.IOException;

/***
 * This exception is used to indicate that the reply from a server could not be interpreted. Most of
 * the NetComponents classes attempt to be as lenient as possible when receiving server replies.
 * Many server implementations deviate from IETF protocol specifications, making it necessary to be
 * as flexible as possible. However, there will be certain situations where it is not possible to
 * continue an operation because the server reply could not be interpreted in a meaningful manner.
 * In these cases, a MalformedServerReplyException should be thrown.
 ***/

public class MalformedServerReplyException extends IOException {

	private static final long serialVersionUID = 6006765264250543945L;

	/*** Constructs a MalformedServerReplyException with no message ***/
	public MalformedServerReplyException() {
		super();
	}

	/***
	 * Constructs a MalformedServerReplyException with a specified message.
	 * 
	 * @param message The message explaining the reason for the exception.
	 ***/
	public MalformedServerReplyException(String message) {
		super(message);
	}

}
