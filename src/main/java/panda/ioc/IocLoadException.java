package panda.ioc;

public class IocLoadException extends Exception {

	private static final long serialVersionUID = 1L;

	public IocLoadException(String message) {
		this(message, null);
	}

	public IocLoadException(String message, Throwable cause) {
		super(message, cause);
	}

}
