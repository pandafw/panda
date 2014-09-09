package panda.mvc;

@SuppressWarnings("serial")
public class MvcConfigException extends RuntimeException {

	public MvcConfigException() {
		super();
	}

	public MvcConfigException(String message, Throwable cause) {
		super(message, cause);
	}

	public MvcConfigException(String message) {
		super(message);
	}

	public MvcConfigException(Throwable cause) {
		super(cause);
	}

}
