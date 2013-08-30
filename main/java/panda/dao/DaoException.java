package panda.dao;

/**
 * 
 * @author yf.frank.wang@gmail.com
 *
 */
@SuppressWarnings("serial")
public class DaoException extends RuntimeException {

	public DaoException() {
		super();
	}

	public DaoException(String message) {
		super(message);
	}

	public DaoException(Throwable cause) {
		super(cause);
	}

	public DaoException(String message, Throwable cause) {
		super(message, cause);
	}

	public static <T> DaoException create(T obj, String fieldName, String name) {
		return new DaoException(String.format("Fail to %s [%s]->[%s]", name,
			obj == null ? "NULL object" : obj.getClass().getName(), fieldName));
	}
	
	public static <T> DaoException create(T obj, String fieldName, String name, Exception e) {
		if (e == null) {
			return create(obj, fieldName, name);
		}

		if (e instanceof DaoException) {
			return (DaoException)e;
		}

		return new DaoException(String.format("Fail to %s [%s]->[%s], because: '%s'", name,
			obj == null ? "NULL object" : obj.getClass().getName(), fieldName, e.getMessage()), e);
	}
}
