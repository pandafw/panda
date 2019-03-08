package panda.ex.sendgrid;

import panda.net.http.HttpException;

public class SendGridException extends HttpException {

	private static final long serialVersionUID = 1L;

	public SendGridException() {
		super();
	}

	public SendGridException(String message, Throwable cause) {
		super(message, cause);
	}

	public SendGridException(String message) {
		super(message);
	}

	public SendGridException(Throwable cause) {
		super(cause);
	}

	public SendGridException(int status, String message, String content) {
		super(status, message, content);
	}

	public SendGridException(int status, String message) {
		super(status, message);
	}

	public SendGridException(int status) {
		super(status);
	}

}
