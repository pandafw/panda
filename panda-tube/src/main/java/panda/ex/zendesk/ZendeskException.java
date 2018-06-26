package panda.ex.zendesk;

import java.io.IOException;

public class ZendeskException extends IOException {
	
	private static final long serialVersionUID = 5512289367718068512L;

	/**
	 * 
	 */
	public ZendeskException() {
		super();
	}

	/**
	 * @param message
	 * @param cause
	 */
	public ZendeskException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * @param message
	 */
	public ZendeskException(String message) {
		super(message);
	}

	/**
	 * @param cause
	 */
	public ZendeskException(Throwable cause) {
		super(cause);
	}

}
