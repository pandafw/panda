package panda.filepool;

import panda.cast.CastContext;
import panda.cast.castor.AnySingleCastor;
import panda.lang.Numbers;


public class FileItemCastor extends AnySingleCastor<FileItem> {
	public static final String KEY = FilePool.class.getName();
	
	public FileItemCastor() {
		super(FileItem.class);
	}

	@Override
	protected FileItem castValue(Object value, CastContext context) {
		FilePool fp = (FilePool)context.get(KEY);
		if (fp == null) {
			throw new NullPointerException("Null FilePool!");
		}
		
		Long id = Numbers.toLong(value.toString());
		if (id == null) {
			return castError(value, context);
		}

		return fp.findFile(id);
	}
}
