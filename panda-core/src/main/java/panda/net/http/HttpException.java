package panda.net.http;

import java.io.IOException;

public class HttpException extends IOException {

	private static final long serialVersionUID = 4035188583429445028L;

	public static final HttpException BAD_REQUEST = new HttpException(HttpStatus.SC_BAD_REQUEST);
	public static final HttpException FORBIDDEN = new HttpException(HttpStatus.SC_FORBIDDEN);
	public static final HttpException NOT_FOUND = new HttpException(HttpStatus.SC_NOT_FOUND);
	public static final HttpException SERVER_ERROR = new HttpException(HttpStatus.SC_INTERNAL_SERVER_ERROR);
	public static final HttpException BAD_GATEWAY = new HttpException(HttpStatus.SC_BAD_GATEWAY);

	private int status;
	private String content;

	public HttpException() {
		super();
	}

	public HttpException(String message, Throwable cause) {
		super(message, cause);
	}

	public HttpException(String message) {
		super(message);
	}

	public HttpException(Throwable cause) {
		super(cause);
	}

	public HttpException(int status) {
		this.status = status;
	}

	public HttpException(int status, String message) {
		super(message);
		this.status = status;
	}

	public HttpException(int status, String message, String content) {
		super(message);
		this.status = status;
		this.content = content;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	/**
	 * @return the content
	 */
	public String getContent() {
		return content;
	}

	/**
	 * @param content the content to set
	 */
	public void setContent(String content) {
		this.content = content;
	}

}
