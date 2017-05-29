package panda.args;

public class CmdLineException extends Exception {

	private static final long serialVersionUID = 1L;

	public CmdLineException(String message, Throwable cause) {
		super(message, cause);
	}

	public CmdLineException(String message) {
		super(message);
	}

	public CmdLineException(Throwable cause) {
		super(cause);
	}

}
