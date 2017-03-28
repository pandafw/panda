package panda.io.stream;

import java.io.IOException;
import java.io.Writer;

public class MultiWriter extends Writer {
	private Writer[] outs;
	
	/**
	 * Constructs a new NullWriter.
	 * @param outs the output writer array
	 */
	public MultiWriter(Writer... outs) {
		this.outs = outs;
	}

	@Override
	public void write(final char[] chr, final int st, final int end) throws IOException {
		for (Writer o : outs) {
			o.write(chr, st, end);
		}
	}

	@Override
	public void flush() throws IOException {
		for (Writer o : outs) {
			o.flush();
		}
	}

	@Override
	public void close() throws IOException {
		for (Writer o : outs) {
			o.close();
		}
	}

}
