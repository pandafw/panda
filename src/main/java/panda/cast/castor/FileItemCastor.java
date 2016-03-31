package panda.cast.castor;

import java.io.InputStream;

import panda.cast.CastContext;
import panda.lang.Numbers;
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
		Long id = null;
		if (value instanceof Number) {
			id = ((Number)value).longValue();
		}
		else if (value instanceof CharSequence) {
			String s = value.toString();
			if (Strings.isEmpty(s)) {
				return defaultValue();
			}
			id = Numbers.toLong(s);
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

		return fp.findFile(id);
	}
}
