package panda.vfs;

import java.io.IOException;
import java.util.Date;
import java.util.List;

public interface FileStore {
	Class<? extends FileItem> getItemType();
	
	FileItem getFile(String name) throws IOException;

	List<FileItem> listFiles() throws IOException;

	List<FileItem> listFiles(String prefix, Date before) throws IOException;
}
