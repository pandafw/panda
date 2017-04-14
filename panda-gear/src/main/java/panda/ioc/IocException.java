package panda.ioc;

public class IocException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public IocException() {
		super();
	}

	public IocException(Throwable cause) {
		super(cause);
	}

	/**
	 * @param message the error message
	 * @param cause the cause exception
	 */
	public IocException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * @param message the error message
	 */
	public IocException(String message) {
		super(message);
	}

}
