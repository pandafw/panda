package panda.cast.castor;

import java.io.IOException;
import java.io.InputStream;

import panda.cast.CastContext;
import panda.lang.Strings;
import panda.vfs.FileItem;
import panda.vfs.FilePool;
import panda.vfs.ProxyFileItem;


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
			if (Strings.isEmpty(id)) {
				return defaultValue();
			}
		}
		else if (value instanceof byte[]) {
			ProxyFileItem fi = new ProxyFileItem();
			fi.setData((byte[])value);
			return fi;
		}
		else if (value instanceof InputStream) {
			ProxyFileItem fi = new ProxyFileItem();
			fi.setInputStream((InputStream)value);
			return fi;
		}
		
		if (id == null) {
			return castError(value, context);
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
