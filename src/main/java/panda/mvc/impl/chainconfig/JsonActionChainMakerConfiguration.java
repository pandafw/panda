package panda.mvc.impl.chainconfig;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import panda.bind.json.Jsons;
import panda.io.Files;
import panda.io.Streams;
import panda.lang.Arrays;
import panda.lang.Charsets;
import panda.lang.Exceptions;
import panda.log.Log;
import panda.log.Logs;

/**
 * 通过json文件获取配置信息.
 * <p/>
 * 默认配置会首先加载,用户文件可以覆盖之
 * 
 */
public class JsonActionChainMakerConfiguration implements ActionChainMakerConfiguration {

	private static final Log log = Logs.getLog(JsonActionChainMakerConfiguration.class);

	protected Map<String, Map<String, Object>> map = new HashMap<String, Map<String, Object>>();

	public JsonActionChainMakerConfiguration(String... paths) {
		try {
			// load default settings
			loadFromPath(getClass().getPackage().getName().replace('.', '/') + "/default-chains.js");
			
			// load customized settings
			if (Arrays.isNotEmpty(paths)) {
				for (String p : paths) {
					loadFromPath(p);
				}
			}
		}
		catch (IOException e) {
			throw Exceptions.wrapThrow(e);
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

		if (log.isDebugEnabled()) {
			log.debug("ActionChain Config:\n" + Jsons.toJson(map));
		}
	}
	
	@SuppressWarnings("unchecked")
	private void loadFromStream(InputStream is) throws IOException {
		try {
			Map jm = Jsons.fromJson(is, Charsets.UTF_8, Map.class);
			if (null != jm && jm.size() > 0) {
				map.putAll(jm);
			}
		}
		finally {
			Streams.safeClose(is);
		}
	}

	@SuppressWarnings("unchecked")
	public List<String> getProcessors(String key) {
		Map<String, Object> config = map.get(key);
		if (config != null && config.containsKey("ps")) {
			return (List<String>)config.get("ps");
		}
		return (List<String>)map.get("default").get("ps");
	}

	public String getErrorProcessor(String key) {
		Map<String, Object> config = map.get(key);
		if (config != null && config.containsKey("error")) {
			return (String)config.get("error");
		}
		return (String)map.get("default").get("error");
	}

}
