package panda.vfs;

import java.io.IOException;
import java.io.InputStream;

public interface FilePool {
	FileItem saveFile(String name, byte[] data, boolean temporary) throws IOException;

	FileItem saveFile(String name, InputStream data, boolean temporary) throws IOException;
	
	FileItem findFile(Long id);
	
	void clean() throws IOException;
}
