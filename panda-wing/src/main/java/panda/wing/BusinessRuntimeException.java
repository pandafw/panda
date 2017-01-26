package panda.wing;

/**
 * {@link panda.wing.action.crud.GenericBulkAction}, {@link panda.wing.action.crud.GenericEditAction} 
 * use WARN level to log this exception
 */
public class BusinessRuntimeException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public BusinessRuntimeException() {
		super();
	}

	public BusinessRuntimeException(String message, Throwable cause) {
		super(message, cause);
	}

	public BusinessRuntimeException(String message) {
		super(message);
	}

	public BusinessRuntimeException(Throwable cause) {
		super(cause);
	}

}