package panda.filepool;

import java.io.IOException;
import java.io.InputStream;
import java.util.Date;

/**
 */
public interface FileItem {
	public static final int TEMPORARY = 0;
	public static final int ARCHIVE = 1;
	

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
	 * @return the flag
	 */
	int getFlag();

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
}

