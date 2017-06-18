package panda.roid;

import java.io.File;

import android.content.Context;
import android.os.Environment;

public class Androids {
	public static final int LOGTAG_MAXLENGTH = 23;

	private static File internalStorageDirectory;
	private static File externalStorageDirectory;
	
	public static void init(Context context) {
		internalStorageDirectory =  context.getFilesDir();
		externalStorageDirectory =  context.getExternalFilesDir(null);
	}

	public static File getInternalStorageDirectory() {
		return internalStorageDirectory;
	}
	
	public static File getExternalStorageDirectory() {
		if (externalStorageDirectory == null) {
			return getExternalStorageRootDirectory();
		}
		return externalStorageDirectory;
	}
	
	public static File getExternalStorageRootDirectory() {
		return Environment.getExternalStorageDirectory();
	}
}
