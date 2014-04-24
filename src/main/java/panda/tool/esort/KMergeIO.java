package panda.tool.esort;

import java.io.IOException;

/**
 * k-merge I/O interface
 * @param <T> object type
 */
public interface KMergeIO<T> {

	/** 
	 * Read object by the given segmentNo
	 * @param segmentNo segment number
	 * @return object
	 * @throws IOException if an I/O error occurs
	 */
	T read(int segmentNo) throws IOException;
	
	/**
	 * Write object 
	 * @param obj object
	 * @throws IOException if an I/O error occurs
	 */
	void write(T obj) throws IOException;

}
