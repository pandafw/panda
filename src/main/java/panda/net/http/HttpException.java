package panda.net.http;

public class HttpException extends RuntimeException {

	private static final long serialVersionUID = 4035188583429445028L;

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
