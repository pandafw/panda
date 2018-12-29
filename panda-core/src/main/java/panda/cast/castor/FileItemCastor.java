package panda.cast.castor;

import java.io.IOException;

import panda.cast.CastContext;
import panda.lang.Strings;
import panda.vfs.FileItem;
import panda.vfs.FilePool;


public class FileItemCastor extends AnySingleCastor<FileItem> {
	public static final String KEY = FilePool.class.getName();
	
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
		
		if (id == null) {
			return castError(value, context);
		}

		if (Strings.isEmpty(id)) {
			return defaultValue();
		}

		FilePool fp = (FilePool)context.get(KEY);
		if (fp == null) {
			throw new NullPointerException("Null FilePool!");
		}

		try {
			return fp.findFile(id);
		}
		catch (IOException e) {
			return castError(value, context, e);
		}
	}
}
