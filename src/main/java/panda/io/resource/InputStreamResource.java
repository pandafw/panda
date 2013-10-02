package panda.io.resource;

import java.io.IOException;
import java.io.InputStream;

/**
 * {@link Resource} implementation for a given InputStream. Should only
 * be used if no specific Resource implementation is applicable.
 * In particular, prefer {@link ByteArrayResource} or any of the
 * file-based Resource implementations where possible.
 *
 * <p>In contrast to other Resource implementations, this is a descriptor
 * for an <i>already opened</i> resource - therefore returning "true" from
 * <code>isOpen()</code>. Do not use it if you need to keep the resource
 * descriptor somewhere, or if you need to read a stream multiple times.
 *
 * @see ByteArrayResource
 * @see ClassPathResource
 * @see FileSystemResource
 * @see UrlResource
 * 
 * @author yf.frank.wang@gmail.com
 */
public class InputStreamResource extends AbstractResource {

	private final InputStream inputStream;

	private final String description;

	private boolean read = false;


	/**
	 * Create a new InputStreamResource.
	 * @param inputStream the InputStream to use
	 */
	public InputStreamResource(InputStream inputStream) {
		this(inputStream, "resource loaded through InputStream");
	}

	/**
	 * Create a new InputStreamResource.
	 * @param inputStream the InputStream to use
	 * @param description where the InputStream comes from
	 */
	public InputStreamResource(InputStream inputStream, String description) {
		if (inputStream == null) {
			throw new IllegalArgumentException("InputStream must not be null");
		}
		this.inputStream = inputStream;
		this.description = (description != null ? description : "");
	}


	/**
	 * This implementation always returns <code>true</code>.
	 */
	@Override
	public boolean exists() {
		return true;
	}

	/**
	 * This implementation always returns <code>true</code>.
	 */
	@Override
	public boolean isOpen() {
		return true;
	}

	/**
	 * This implementation throws IllegalStateException if attempting to
	 * read the underlying stream multiple times.
	 */
	public InputStream getInputStream() throws IOException, IllegalStateException {
		if (this.read) {
			throw new IllegalStateException("InputStream has already been read - " +
					"do not use InputStreamResource if a stream needs to be read multiple times");
		}
		this.read = true;
		return this.inputStream;
	}

	/**
	 * This implementation returns the passed-in description, if any.
	 */
	public String getDescription() {
		return this.description;
	}


	/**
	 * This implementation compares the underlying InputStream.
	 */
	@Override
	public boolean equals(Object obj) {
		return (obj == this ||
		    (obj instanceof InputStreamResource && ((InputStreamResource) obj).inputStream.equals(this.inputStream)));
	}

	/**
	 * This implementation returns the hash code of the underlying InputStream.
	 */
	@Override
	public int hashCode() {
		return this.inputStream.hashCode();
	}

}
