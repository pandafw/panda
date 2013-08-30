package panda.io;

import java.io.Closeable;
import java.io.Flushable;
import java.io.IOException;
import java.io.OutputStream;

/**
 * BitWriter
 * @author yf.frank.wang@gmail.com
 */
public class BitWriter implements Closeable, Flushable {

	private final static int MASK = 0x80;
	
	private OutputStream ostream;

	private int rack;
	
	private int mask;
	
	/**
	 * Constructor
	 * @param ostream output stream
	 */
	public BitWriter(OutputStream ostream) {
		this.ostream = ostream;
		reset();
	}
	
	/**
	 * get output stream
	 * @return output stream
	 */
	public OutputStream getOutputStream() {
		return ostream;
	}
	
	/**
	 * reset
	 */
	public void reset() {
		rack = 0;
		mask = MASK;
	}
	
	/**
	 * close the output stream
	 * @throws IOException if an I/O error occurs
	 */
	public void close() throws IOException {
		if (ostream != null) {
			flush();
			ostream.close();
		}
	}
	
	/**
	 * flush 
	 * @throws IOException if an I/O error occurs
	 */
	public void flush() throws IOException {
		if (mask != 0x80) {
			ostream.write(rack);
		}
		reset();
	}

	/**
	 * write bit
	 * @param bit  bit
	 * @throws IOException if an I/O error occurs
	 */
	public void writeBit(int bit) throws IOException {
		if (bit != 0) {
			rack |= mask;
		}
		
		mask >>= 1;
		if (mask == 0) {
			ostream.write(rack);
			reset();
		}
	}

	/**
	 * write bits
	 * @param code bits code
	 * @param size bits size
	 * @throws IOException if an I/O error occurs
	 */
	public void writeBits(long code, int size) throws IOException {
		if (size < 0 || size > 64) {
			throw new IllegalArgumentException("Illegal write bits size (0 < size < 65): " + size);
		}

		long mask;
		mask = 1L << (size - 1);
		size = 0;
		while (mask != 0) {
			writeBit((int)(mask & code));
			mask >>= 1;
			size++;
		}
	}
	
	/**
	 * write bits
	 * @param bs	bits array
	 * @param size	write size
	 * @throws IOException if an I/O error occurs
	 */
	public void writeBits(byte[] bs, int size) throws IOException {
		if (size < 0 || size > bs.length) {
			throw new IllegalArgumentException("Illegal write bits size (0 < size < byte[" + bs.length + "].length): " + size);
		}

		for (int i = 0; i < size; i++) {
			writeBit(bs[i]);
		}
	}

	/**
	 * write bits 
	 * @param bs bits array
	 * @throws IOException if an I/O error occurs
	 */
	public void writeBits(byte[] bs) throws IOException {
		writeBits(bs, bs.length);
	}
	
}
