package panda.roid.log;

import panda.io.FileNames;
import panda.lang.Strings;
import panda.log.impl.FileLogAdapter;
import panda.roid.Androids;


public class AndroidFileLogAdapter extends FileLogAdapter {
	private Boolean ext;
	private String tag;
	
	@Override
	protected void setProperty(String name, String value) {
		if ("tag".equalsIgnoreCase(name)) {
			tag = Strings.isEmpty(value) ? getClass().getSimpleName() : value;
		}
		else if ("ext".equalsIgnoreCase(name)) {
			if ("true".equalsIgnoreCase(value)) {
				ext = true;
			}
			else if ("false".equalsIgnoreCase(value)) {
				ext = false;
			}
		}
		else {
			super.setProperty(name, value);
		}
	}

	@Override
	protected String getAbsolutePath() {
		if (Boolean.FALSE.equals(ext)) {
			if (Androids.getInternalStorageDirectory() != null) {
				return FileNames.concat(Androids.getInternalStorageDirectory().getAbsolutePath(), path);
			}
		}
		return FileNames.concat(Androids.getExternalStorageDirectory().getAbsolutePath(), path);
	}
	
	@Override
	protected void rollOver() {
		try {
			super.rollOver();
		}
		catch (RuntimeException e) {
			android.util.Log.e(tag, getClass().getSimpleName() + ": " + e.getMessage(), e);
			throw e;
		}
	}
}
