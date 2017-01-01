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
	 */
	byte[] getData() throws IOException;

	/**
	 * @return the input stream
	 */
	InputStream getInputStream() throws IOException;

	/**
	 * save data
	 */
	void save(byte[] data) throws IOException;
	
	/**
	 * save data
	 */
	void save(InputStream data) throws IOException;
	
	/**
	 * delete self
	 */
	void delete() throws IOException;
}

