package panda.roid.log;

import panda.io.FileNames;
import panda.lang.Booleans;
import panda.log.impl.FileLogAdapter;
import panda.roid.Androids;


public class AndroidFileLogAdapter extends FileLogAdapter {
	private boolean internal;
	
	@Override
	protected void setProperty(String name, String value) {
		if ("internal".equalsIgnoreCase(name)) {
			internal = Booleans.toBoolean(value);
		}
		else {
			super.setProperty(name, value);
		}
	}

	@Override
	protected String getAbsolutePath() {
		// Androids.init(context) should be called before Logs.getLog().

		if (internal && Androids.getInternalStorageDirectory() != null) {
			return FileNames.concat(Androids.getInternalStorageDirectory().getAbsolutePath(), path);
		}
		return FileNames.concat(Androids.getExternalStorageDirectory().getAbsolutePath(), path);
	}
}
