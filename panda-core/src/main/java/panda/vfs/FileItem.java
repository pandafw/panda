package panda.vfs;

import java.io.IOException;
import java.io.InputStream;
import java.util.Date;

/**
 */
public interface FileItem {
	public static final String NAME = "name";
	public static final String SIZE = "size";
	public static final String DATE = "date";
	public static final String TYPE = "type";

	/**
	 * @return the name
	 */
	String getName();

	/**
	 * @return the size
	 */
	int getSize();

	/**
	 * @return the date
	 */
	Date getDate();

	/**
	 * @return the type
	 */
	String getType();

	/**
	 * @return exists
	 */
	boolean isExists();
	
	/**
	 * @return the data
	 * @throws IOException if an IO error occurs.
	 */
	byte[] data() throws IOException;

	/**
	 * @return the input stream
	 * @throws IOException if an IO error occurs.
	 */
	InputStream open() throws IOException;

	/**
	 * save data
	 * 
	 * @param data the data to save
	 * @throws IOException if an IO error occurs.
	 */
	void save(byte[] data) throws IOException;
	
	/**
	 * save data
	 * 
	 * @param data the data to save
	 * @throws IOException if an IO error occurs.
	 */
	void save(InputStream data) throws IOException;
	
	/**
	 * delete self
	 * @throws IOException if an IO error occurs.
	 */
	void delete() throws IOException;
}

