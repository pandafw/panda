package panda.filepool;

import java.io.IOException;
import java.io.InputStream;

import panda.mvc.adaptor.multipart.FileItemStream;

public interface FilePool {
	FileItem saveFile(String name, byte[] data, boolean temporary) throws IOException;

	FileItem saveFile(String name, InputStream data, boolean temporary) throws IOException;
	
	FileItem saveFile(FileItemStream fis, boolean temporary) throws IOException;

	FileItem findFile(Long id);
}
