package panda.ioc.loader;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.util.Arrays;
import java.util.Collection;

import panda.bind.json.JsonObject;
import panda.io.Files;
import panda.io.Streams;
import panda.lang.Charsets;
import panda.lang.Exceptions;
import panda.log.Log;
import panda.log.Logs;

/**
 * 从 Json 文件中读取配置信息。 支持 Merge with parent ，利用 MapLoader
 * <p>
 * 注，如果 JSON 配置文件被打入 Jar 包中，这个加载器将不能正常工作
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
			log.debugf("Loaded %d bean define from path=%s --> %s", 
				beans.size(), Arrays.toString(paths), beans.keySet());
		}
	}

	private void loadFromPath(String path) throws IOException {
		File p = new File(path);
		if (Files.isDirectory(p)) {
			Collection<File> fs = Files.listFiles(p, new String[] { "js", "json" }, true);
			for (File f : fs) {
				InputStream is = new FileInputStream(f);
				loadFromStream(is);
			}
		}
		else {
			InputStream is = Streams.getStream(path);
			loadFromStream(is);
		}
	}
	
	private void loadFromStream(InputStream is) throws IOException {
		try {
			JsonObject jo = JsonObject.fromJson(is, Charsets.UTF_8);
			if (null != jo && jo.size() > 0) {
				initialize(jo);
			}
		}
		finally {
			Streams.safeClose(is);
		}
	}
	
	private void loadFromReader(Reader reader) {
		JsonObject jo = JsonObject.fromJson(reader);
		if (null != jo && jo.size() > 0) {
			initialize(jo);
		}
	}
}
