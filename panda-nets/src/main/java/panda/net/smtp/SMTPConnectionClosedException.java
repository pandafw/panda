package panda.net.smtp;

import java.io.IOException;

/***
 * SMTPConnectionClosedException is used to indicate the premature or unexpected closing of an SMTP
 * connection resulting from a {@link panda.net.smtp.SMTPReply#SERVICE_NOT_AVAILABLE
 * SMTPReply.SERVICE_NOT_AVAILABLE } response (SMTP reply code 421) to a failed SMTP command. This
 * exception is derived from IOException and therefore may be caught either as an IOException or
 * specifically as an SMTPConnectionClosedException.
 * 
 * @see SMTP
 * @see SMTPClient
 ***/

public final class SMTPConnectionClosedException extends IOException {

	private static final long serialVersionUID = 626520434326660627L;

	/*** Constructs a SMTPConnectionClosedException with no message ***/
	public SMTPConnectionClosedException() {
		super();
	}

	/***
	 * Constructs a SMTPConnectionClosedException with a specified message.
	 * 
	 * @param message The message explaining the reason for the exception.
	 ***/
	public SMTPConnectionClosedException(String message) {
		super(message);
	}

}
