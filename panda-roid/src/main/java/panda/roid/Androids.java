package panda.roid;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import android.content.Context;
import android.content.res.AssetManager;
import android.os.Environment;
import panda.io.Settings;
import panda.io.Streams;
import panda.log.Logs;

public class Androids {
	public static final int LOGTAG_MAXLENGTH = 23;

	private static boolean initialized;
	private static File internalStorageDirectory;
	private static File externalStorageDirectory;
	
	public static void init(Context context) {
		if (initialized) {
			return;
		}
		
		internalStorageDirectory =  context.getFilesDir();
		externalStorageDirectory =  context.getExternalFilesDir(null);
		initLogs(context);
		initialized = true;
	}

	private static void initLogs(Context context) {
		// Because ClassLoader.getResourceAsStream() always return null,
		// we use assets/panda-logging.properties to initialize log.
		AssetManager am = context.getAssets();
		InputStream is = null;
		try {
			is = am.open(Logs.CONFIG);
			Settings ss = new Settings();
			ss.load(is, Logs.CONFIG);
			Logs.init(ss);
		}
		catch (IOException e) {
			// skip
		}
		finally {
			Streams.safeClose(is);
		}
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
