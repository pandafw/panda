package panda.mvc.bind.adapter;

import panda.bind.adapter.IncludePropertyFilter;
import panda.vfs.FileItem;

/**
 */
public class FileItemAdapter extends IncludePropertyFilter<FileItem> {
	/**
	 * Constructor
	 */
	public FileItemAdapter() {
		includes.add("id");
		includes.add("name");
		includes.add("size");
		includes.add("date");
		includes.add("contentType");
		includes.add("exists");
		includes.add("temporary");
	}
}

