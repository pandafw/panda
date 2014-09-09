package panda.mvc.adaptor.multipart;

/**
 * Interface that will indicate that {@link FileItemStream} implementations will
 * accept the headers read for the item.
 * 
 * @see FileItemStream
 */
public interface FileItemHeadersSupport {

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

}
