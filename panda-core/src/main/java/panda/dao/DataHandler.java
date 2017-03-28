package panda.dao;


/**
 * DataHandler
 * @param <T> data type
 */
public interface DataHandler<T> {
	/**
	 * handle data
	 * @param data data
	 * @return false to stop the process
	 * @throws Exception if an error occurred
	 */
	boolean handle(T data) throws Exception;
}
