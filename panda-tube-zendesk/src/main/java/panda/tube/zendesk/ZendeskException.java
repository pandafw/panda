package panda.tube.zendesk;

import panda.net.http.HttpException;

public class ZendeskException extends HttpException {
	
	private static final long serialVersionUID = 5512289367718068512L;

	public ZendeskException() {
		super();
	}

	public ZendeskException(String message, Throwable cause) {
		super(message, cause);
	}

	public ZendeskException(String message) {
		super(message);
	}

	public ZendeskException(Throwable cause) {
		super(cause);
	}

	public ZendeskException(int status, String message, String content) {
		super(status, message, content);
	}

	public ZendeskException(int status, String message) {
		super(status, message);
	}

	public ZendeskException(int status) {
		super(status);
	}

}
