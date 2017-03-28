package panda.vfs;

import java.io.IOException;
import java.io.InputStream;
import java.util.Date;

/**
 */
public interface FileItem {
	/**
	 * @return the id
	 */
	Long getId();

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
	 * @return the contentType
	 */
	String getContentType();

	/**
	 * @return true if this is a temporary file
	 */
	boolean isTemporary();

	/**
	 * @return exists
	 */
	boolean isExists();
	
	/**
	 * @return the data
	 * @throws IOException if an IO error occurs.
	 */
	byte[] getData() throws IOException;

	/**
	 * @return the input stream
	 * @throws IOException if an IO error occurs.
	 */
	InputStream getInputStream() throws IOException;

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

