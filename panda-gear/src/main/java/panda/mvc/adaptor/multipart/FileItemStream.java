package panda.mvc.adaptor.multipart;

import java.io.IOException;
import java.io.InputStream;

import javax.servlet.http.HttpServletRequest;

/**
 * <p>
 * This interface provides access to a file or form item that was received within a
 * <code>multipart/form-data</code> POST request. The items contents are retrieved by calling
 * {@link #openStream()}.
 * </p>
 * <p>
 * Instances of this class are created by accessing the iterator, returned by
 * {@link FileUploader#getItemIterator(HttpServletRequest)}.
 * </p>
 * <p>
 * <em>Note</em>: There is an interaction between the iterator and its associated instances of
 * {@link FileItemStream}: By invoking {@link java.util.Iterator#hasNext()} on the iterator, you
 * discard all data, which hasn't been read so far from the previous data.
 * </p>
 */
public interface FileItemStream {
	/**
	 * Returns the collection of headers defined locally within this item.
	 * 
	 * @return the {@link FileItemHeaders} present for this item.
	 */
	FileItemHeaders getHeaders();

	/**
	 * Sets the headers read from within an item. Implementations of {@link FileItemStream} should
	 * implement this interface to be able to get the raw headers found within the item header
	 * block.
	 * 
	 * @param headers the instance that holds onto the headers for this instance.
	 */
	void setHeaders(FileItemHeaders headers);

	/**
	 * Creates an {@link InputStream}, which allows to read the items contents.
	 * 
	 * @return The input stream, from which the items data may be read.
	 * @throws IllegalStateException The method was already invoked on this item. It is not possible
	 *             to recreate the data stream.
	 * @throws IOException An I/O error occurred.
	 */
	InputStream openStream() throws IOException;

	/**
	 * Returns the content type passed by the browser or <code>null</code> if not defined.
	 * 
	 * @return The content type passed by the browser or <code>null</code> if not defined.
	 */
	String getContentType();

	/**
	 * Returns the original filename in the client's filesystem, as provided by the browser (or
	 * other client software). In most cases, this will be the base file name, without path
	 * information. However, some clients, such as the Opera browser, do include path information.
	 * 
	 * @return The original filename in the client's filesystem.
	 */
	String getName();

	/**
	 * Returns the name of the field in the multipart form corresponding to this file item.
	 * 
	 * @return The name of the form field.
	 */
	String getFieldName();

	/**
	 * Determines whether or not a <code>FileItem</code> instance represents a simple form field.
	 * 
	 * @return <code>true</code> if the instance represents a simple form field; <code>false</code>
	 *         if it represents an uploaded file.
	 */
	boolean isFormField();

}
