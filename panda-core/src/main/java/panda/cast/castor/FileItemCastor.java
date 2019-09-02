package panda.cast.castor;

import java.io.IOException;

import panda.cast.CastContext;
import panda.lang.Strings;
import panda.vfs.FileItem;
import panda.vfs.FileStore;


public class FileItemCastor extends AnySingleCastor<FileItem> {
	public static final String KEY = FileStore.class.getName();
	
	public FileItemCastor() {
		super(FileItem.class);
	}

	@Override
	protected FileItem castValue(Object value, CastContext context) {
		String id = null;
		if (value instanceof Number) {
			id = String.valueOf(value);
		}
		else if (value instanceof CharSequence) {
			id = value.toString();
		}
		else {
			return castError(value, context);
		}
		
		if (Strings.isEmpty(id)) {
			return defaultValue();
		}

		FileStore fs = (FileStore)context.get(KEY);
		if (fs == null) {
			throw new NullPointerException("Null " + KEY);
		}

		try {
			return fs.getFile(id);
		}
		catch (IOException e) {
			return castError(value, context, e);
		}
	}
}
