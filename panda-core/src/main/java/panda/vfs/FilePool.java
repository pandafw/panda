package panda.vfs;

import java.io.IOException;
import java.io.InputStream;

public interface FilePool {
	Class<? extends FileItem> getItemType();
	
	FileItem saveFile(String name, byte[] data) throws IOException;

	FileItem saveFile(String name, InputStream data) throws IOException;
	
	FileItem findFile(Long id);
	
	void clean() throws IOException;
}
