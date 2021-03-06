package panda.ioc.loader;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.util.Collection;

import panda.bind.json.JsonObject;
import panda.io.Files;
import panda.io.Streams;
import panda.lang.Arrays;
import panda.lang.Charsets;
import panda.lang.Exceptions;
import panda.lang.Strings;
import panda.log.Log;
import panda.log.Logs;

/**
 * Load Ioc configurations from Json.
 */
public class JsonIocLoader extends MapIocLoader {
	private static final Log log = Logs.getLog(JsonIocLoader.class);

	public JsonIocLoader(Reader reader) {
		loadFromReader(reader);
		if (log.isDebugEnabled()) {
			log.debugf("Loaded %d bean define from reader --\n%s", beans.size(), beans.keySet());
		}
	}

	public JsonIocLoader(String... paths) {
		try {
			for (String p : paths) {
				loadFromPath(p);
			}
		}
		catch (IOException e) {
			throw Exceptions.wrapThrow(e);
		}
		
		if (log.isDebugEnabled()) {
			log.debug("Loaded " + beans.size() + " bean definitions from " 
					+ Arrays.toString(paths)
					+ Streams.EOL + " - "
					+ Strings.join(beans.keySet(), Streams.EOL + " - "));
		}
	}

	private void loadFromPath(String path) throws IOException {
		File p = new File(path);
		if (Files.isDirectory(p)) {
			Collection<File> fs = Files.listFiles(p, true, "js", "json");
			for (File f : fs) {
				InputStream is = new FileInputStream(f);
				loadFromStream(is);
			}
		}
		else {
			InputStream is = Streams.openInputStream(path);
			loadFromStream(is);
		}
	}
	
	private void loadFromStream(InputStream is) throws IOException {
		try {
			JsonObject jo = JsonObject.fromJson(is, Charsets.UTF_8);
			if (jo != null && jo.size() > 0) {
				initialize(jo);
			}
		}
		finally {
			Streams.safeClose(is);
		}
	}
	
	private void loadFromReader(Reader reader) {
		JsonObject jo = JsonObject.fromJson(reader);
		if (jo != null && jo.size() > 0) {
			initialize(jo);
		}
	}
}
