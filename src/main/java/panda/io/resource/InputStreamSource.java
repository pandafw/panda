package panda.io.resource;

import java.io.IOException;
import java.io.InputStream;

/**
 * Simple interface for objects that are sources for an {@link InputStream}.
 *
 * <p>This is the base interface for Spring's more extensive {@link Resource} interface.
 *
 * <p>For single-use streams, {@link InputStreamResource} can be used for any
 * given <code>InputStream</code>. Spring's {@link ByteArrayResource} or any
 * file-based <code>Resource</code> implementation can be used as a concrete
 * instance, allowing one to read the underlying content stream multiple times.
 * This makes this interface useful as an abstract content source for mail
 * attachments, for example. 
 *
 * @see java.io.InputStream
 * @see Resource
 * @see InputStreamResource
 * @see ByteArrayResource
 * 
 * @author yf.frank.wang@gmail.com
 */
public interface InputStreamSource {

	/**
	 * Return an {@link InputStream}.
	 * <p>It is expected that each call creates a <i>fresh</i> stream.
	 * <p>This requirement is particularly important when you consider an API such
	 * as JavaMail, which needs to be able to read the stream multiple times when
	 * creating mail attachments. For such a use case, it is <i>required</i>
	 * that each <code>getInputStream()</code> call returns a fresh stream.
	 * @throws IOException if the stream could not be opened
	 */
	InputStream getInputStream() throws IOException;

}
