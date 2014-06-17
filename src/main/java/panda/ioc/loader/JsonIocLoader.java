package panda.ioc.loader;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import panda.bind.json.Jsons;
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
@SuppressWarnings("unchecked")
public class JsonIocLoader extends MapIocLoader {

	private static final Log log = Logs.getLog(JsonIocLoader.class);

	public JsonIocLoader(Reader reader) {
		loadFromReader(reader);
		if (log.isDebugEnabled()) {
			log.debugf("Loaded %d bean define from reader --\n%s", getMap().size(), getMap().keySet());
		}
	}

	public JsonIocLoader(String... paths) {
		this.setMap(new HashMap<String, Map<String, Object>>());
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
				getMap().size(), Arrays.toString(paths), getMap().keySet());
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
			Map<String, Map<String, Object>> map = (Map<String, Map<String, Object>>)Jsons.fromJson(is, Charsets.UTF_8, Map.class);
			if (null != map && map.size() > 0) {
				getMap().putAll(map);
			}
		}
		finally {
			Streams.safeClose(is);
		}
	}
	
	private void loadFromReader(Reader reader) {
		Map<String, Map<String, Object>> map = (Map<String, Map<String, Object>>)Jsons.fromJson(reader, Map.class);
		if (null != map && map.size() > 0) {
			getMap().putAll(map);
		}
	}
}
