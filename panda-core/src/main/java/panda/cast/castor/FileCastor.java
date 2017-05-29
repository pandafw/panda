package panda.cast.castor;

import java.io.File;

import panda.cast.CastContext;


public class FileCastor extends AnySingleCastor<File> {
	public FileCastor() {
		super(File.class);
	}

	@Override
	protected File castValue(Object value, CastContext context) {
		if (value instanceof CharSequence) {
			String s = value.toString();
			return new File(s);
		}
		
		return castError(value, context);
	}
}
