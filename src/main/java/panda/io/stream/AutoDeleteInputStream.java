package panda.io.stream;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FilterInputStream;
import java.io.IOException;


/**
 * automatically delete file when the input stream is closed.
 * @author yf.frank.wang@gmail.com
 */
public class AutoDeleteInputStream extends FilterInputStream {
	
	private File file;
	
	protected AutoDeleteInputStream(File f) throws FileNotFoundException {
		super(new FileInputStream(f));
		this.file = f;
	}
	
	public int read() throws IOException {
		return super.read();
	}
	
	public int read(byte[] b) throws IOException {
		return super.read(b);
	}
	
	public int read(byte[] b, int off, int len) throws IOException {
		return super.read(b, off, len);
	}
	
	public void close() throws IOException {
		super.close();
		file.delete();
	}
	
	protected void finalize() throws Throwable {
		file.delete();
		super.finalize();
	}
	
}
