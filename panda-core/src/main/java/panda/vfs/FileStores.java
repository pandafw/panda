package panda.vfs;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import panda.io.Streams;

public abstract class FileStores {
	/**
	 * read file content to byte array
	 * 
	 * @param file file
	 * @return byte array
	 * @throws FileNotFoundException if file not found
	 * @throws IOException in case of I/O errors
	 */
	public static byte[] toByteArray(FileItem file) throws FileNotFoundException, IOException {
		InputStream fis = file.open();
		try {
			byte[] b = Streams.toByteArray(fis);
			return b;
		}
		finally {
			Streams.safeClose(fis);
		}
	}

	public static void copy(FileItem src, File des) throws IOException {
		InputStream in = src.open();
		try {
			Streams.copy(in, des);
		}
		finally {
			Streams.safeClose(in);
		}
	}
	
	public static void copy(File src, FileItem des) throws IOException {
		InputStream in = new FileInputStream(src);
		try {
			des.save(in);
		}
		finally {
			Streams.safeClose(in);
		}
	}
	
	public static void safeDelete(FileItem f) {
		if (f == null || !f.isExists()) {
			return;
		}
		
		try {
			f.delete();
		}
		catch (Throwable e) {
			// ignore
		}
	}
}
