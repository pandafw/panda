package panda.io.stream;

import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;

/**
 * BitReader
 * @author yf.frank.wang@gmail.com
 */
public class BitReader implements Closeable {

	private final static int MASK = 0x80;
	
	private InputStream istream;

	private int rack;
	
	private int mask;
	
	/**
	 * Constructor
	 * @param istream input stream
	 */
	public BitReader(InputStream istream) {
		this.istream = istream;
		reset();
	}
	
	/**
	 * get input stream
	 * @return input stream
	 */
	public InputStream getInputStream() {
		return istream;
	}
	
	/**
	 * reset
	 */
	public void reset() {
		rack = 0;
		mask = MASK;
	}
	
	/**
	 * close the input stream
	 * @throws IOException if an I/O error occurs
	 */
	public void close() throws IOException {
		if (istream != null) {
			istream.close();
		}
	}

	/**
	 * read a bit
	 * @return bit
	 * @throws IOException if an I/O error occurs
	 */
	public int readBit() throws IOException {
		if (mask == MASK) {
			if ((rack = istream.read()) == -1) 
				return -1;
		}
	
		int value = rack & mask;
		
		mask >>= 1;
		if (mask == 0) { 
			mask = MASK;
		}

		return (value != 0 ? 1 : 0);
	}

	
	/**
	 * read bits
	 * @param	size read bits size.
	 * @return bits array
	 * @throws IOException if an I/O error occurs
	 */
	public byte[] readBits(int size) throws IOException { 
		if (size < 0) {
			throw new IllegalArgumentException("Negative read bits size: " + size);
		}

		ByteArrayOutputStream baos = new ByteArrayOutputStream(size);
		
		int b = 0;
		for (int i = 0; i < size; i++) {
			if ((b = readBit()) == -1) {
				break;
			}
			baos.write(b);
		}
		
		return baos.toByteArray();
	}
}
