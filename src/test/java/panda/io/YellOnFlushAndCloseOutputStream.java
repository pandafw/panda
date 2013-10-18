package panda.io;

import java.io.IOException;
import java.io.OutputStream;

import junit.framework.AssertionFailedError;
import panda.io.stream.ProxyOutputStream;

/**
 * Helper class for checking behaviour of IO classes.
 */
public class YellOnFlushAndCloseOutputStream extends ProxyOutputStream {

	private boolean yellForFlush;
	private boolean yellForClose;

	/**
	 * @param proxy OutputStream to delegate to.
	 * @param yellForFlush True if flush() is forbidden
	 * @param yellForClose True if close() is forbidden
	 */
	public YellOnFlushAndCloseOutputStream(final OutputStream proxy, final boolean yellForFlush,
			final boolean yellForClose) {
		super(proxy);
		this.yellForFlush = yellForFlush;
		this.yellForClose = yellForClose;
	}

	/** @see java.io.OutputStream#flush() */
	@Override
	public void flush() throws IOException {
		if (yellForFlush) {
			throw new AssertionFailedError("flush() was called on OutputStream");
		}
		super.flush();
	}

	/** @see java.io.OutputStream#close() */
	@Override
	public void close() throws IOException {
		if (yellForClose) {
			throw new AssertionFailedError("close() was called on OutputStream");
		}
		super.close();
	}

	public void off() {
		yellForFlush = false;
		yellForClose = false;
	}

}
