package panda.mvc.impl;

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
import panda.ioc.annotation.IocBean;
import panda.lang.Arrays;
import panda.lang.Charsets;
import panda.lang.ClassLoaders;
import panda.lang.Exceptions;
import panda.lang.Strings;
import panda.log.Log;
import panda.log.Logs;
import panda.mvc.ActionChain;
import panda.mvc.ActionChainMaker;
import panda.mvc.ActionInfo;
import panda.mvc.MvcConfig;
import panda.mvc.processor.AdaptProcessor;
import panda.mvc.processor.FatalProcessor;
import panda.mvc.processor.InvokeProcessor;
import panda.mvc.processor.LayoutProcessor;
import panda.mvc.processor.LocaleProcessor;
import panda.mvc.processor.ValidateProcessor;
import panda.mvc.processor.ViewProcessor;

@IocBean(type=ActionChainMaker.class)
public class DefaultActionChainMaker implements ActionChainMaker {

	private static final Log log = Logs.getLog(DefaultActionChainMaker.class);

	protected Map<String, List<String>> map = new HashMap<String, List<String>>();

	public DefaultActionChainMaker() {
		// load default settings
		setDefault();
		
		// external
		setExternal();
	}

	protected void setDefault() {
		List<String> defs = Arrays.toList(
			FatalProcessor.class.getName(),
			AdaptProcessor.class.getName(),
			LocaleProcessor.class.getName(),
			LayoutProcessor.class.getName(),
			ValidateProcessor.class.getName(),
			InvokeProcessor.class.getName(),
			ViewProcessor.class.getName()
		);
		map.put("default", defs);
	}

	protected void setExternal() {
		InputStream is = null;
		try {
			// load default custom settings
			is = ClassLoaders.getResourceAsStream("mvc-chains.json");
			if (is != null) {
				loadFromStream(is);
			}
		}
		catch (IOException e) {
			throw Exceptions.wrapThrow(e);
		}
		finally {
			Streams.safeClose(is);
		}
	}

	public void setChains(String... paths) {
		try {
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

	private List<String> getProcessors(String key) {
		if (Strings.isEmpty(key)) {
			key = "default";
		}

		return (List<String>)map.get(key);
	}
	
	public ActionChain eval(MvcConfig config, ActionInfo ai) {
		List<String> procs = getProcessors(ai.getChainName());
		ActionChain chain = new DefaultActionChain(ai, procs);
		return chain;
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
}
