package panda.dao;


/**
 * DataHandler
 * @param <T> data type
 * @author yf.frank.wang@gmail.com
 */
public interface DataHandler<T> {
	/**
	 * handle data
	 * @param data data
	 * @return false to stop the process
	 */
	boolean handleData(T data) throws Exception;
}
