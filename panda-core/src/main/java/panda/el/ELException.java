package panda.el;

public class ELException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public ELException(String message, Throwable cause) {
		super(message, cause);
	}

	public ELException(String format, Object... args) {
		super(String.format(format, args));
	}

	public ELException(String message) {
		super(message);
	}

	public ELException(Throwable cause) {
		super(cause);
	}

}
