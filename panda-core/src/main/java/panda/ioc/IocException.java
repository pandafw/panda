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
	 * @param message
	 * @param cause
	 */
	public IocException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * @param message
	 */
	public IocException(String message) {
		super(message);
	}

}
