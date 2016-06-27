package panda.net.http;


public class HttpException extends RuntimeException {

	private static final long serialVersionUID = 4035188583429445028L;

	public static final HttpException BAD_REQUEST = new HttpException(HttpStatus.SC_BAD_REQUEST);
	public static final HttpException FORBIDDEN = new HttpException(HttpStatus.SC_FORBIDDEN);
	public static final HttpException NOT_FOUND = new HttpException(HttpStatus.SC_NOT_FOUND);
	public static final HttpException SERVER_ERROR = new HttpException(HttpStatus.SC_INTERNAL_SERVER_ERROR);
	public static final HttpException BAD_GATEWAY = new HttpException(HttpStatus.SC_BAD_GATEWAY);

	private int status;

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public HttpException(int status) {
		this.status = status;
	}

	public HttpException(int status, String fmt, Object... args) {
		super(String.format(fmt, args));
		this.status = status;
	}

}
