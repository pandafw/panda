package panda.filepool;

import java.io.IOException;
import java.io.InputStream;

import panda.mvc.adaptor.multipart.FileItemStream;

public interface FilePool {
	FileItem saveFile(String name, InputStream body, boolean temporary) throws IOException;
	
	FileItem saveFile(FileItemStream fis, boolean temporary) throws IOException;

	InputStream openFile(Long id) throws IOException;

	InputStream openFile(FileItem fi) throws IOException;
}
