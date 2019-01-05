package panda.vfs;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import panda.io.Streams;

public abstract class FileStores {
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
