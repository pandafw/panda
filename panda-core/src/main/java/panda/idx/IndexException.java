package panda.idx;

public class IndexException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public IndexException(String message, Throwable cause) {
		super(message, cause);
	}

	public IndexException(String format, Object... args) {
		super(String.format(format, args));
	}

	public IndexException(String message) {
		super(message);
	}

	public IndexException(Throwable cause) {
		super(cause);
	}
}
