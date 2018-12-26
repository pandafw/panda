package panda.vfs;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public interface FilePool {
	Class<? extends FileItem> getItemType();
	
	FileItem saveFile(String name, byte[] data) throws IOException;

	FileItem saveFile(String name, InputStream data) throws IOException;
	
	FileItem findFile(String id) throws IOException;

	List<FileItem> listFiles() throws IOException;
	
	int clean() throws IOException;
}
