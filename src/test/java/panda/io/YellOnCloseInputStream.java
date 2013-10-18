package panda.io;

import java.io.IOException;
import java.io.InputStream;

import junit.framework.AssertionFailedError;
import panda.io.stream.ProxyInputStream;

/**
 * Helper class for checking behaviour of IO classes.
 */
public class YellOnCloseInputStream extends ProxyInputStream {

	/**
	 * @param proxy InputStream to delegate to.
	 */
	public YellOnCloseInputStream(final InputStream proxy) {
		super(proxy);
	}

	/** @see java.io.InputStream#close() */
	@Override
	public void close() throws IOException {
		throw new AssertionFailedError("close() was called on OutputStream");
	}

}
