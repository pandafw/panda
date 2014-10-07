package panda.mvc.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
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
import panda.lang.Classes;
import panda.lang.Exceptions;
import panda.lang.Strings;
import panda.log.Log;
import panda.log.Logs;
import panda.mvc.ActionChain;
import panda.mvc.ActionChainMaker;
import panda.mvc.ActionInfo;
import panda.mvc.MvcConfig;
import panda.mvc.Processor;

@IocBean(type=ActionChainMaker.class)
public class DefaultActionChainMaker implements ActionChainMaker {

	private static final Log log = Logs.getLog(DefaultActionChainMaker.class);

	protected Map<String, Map<String, Object>> map = new HashMap<String, Map<String, Object>>();

	public DefaultActionChainMaker() {
		// load default settings
		setChains(DefaultActionChainMaker.class.getPackage().getName().replace('.', '/') + "/chains.json");
		
		// external
		setExternal();
	}
	
	protected void setExternal() {
		try {
			// load default custom settings
			InputStream is = ClassLoaders.getResourceAsStream("mvc-chains.json");
			if (is != null) {
				loadFromStream(is);
			}
		}
		catch (IOException e) {
			throw Exceptions.wrapThrow(e);
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

	public ActionChain eval(MvcConfig config, ActionInfo ai) {
		try {
			List<Processor> procs = new ArrayList<Processor>();
			for (String name : getProcessors(ai.getChainName())) {
				Processor processor = initProcessor(name, config, ai);
				procs.add(processor);
			}

			ActionChain chain = new DefaultActionChain(procs);
			return chain;
		}
		catch (Throwable e) {
			if (log.isDebugEnabled()) {
				log.debugf("Failed to eval action chain for %s/%s", ai.getChainName(), ai.getMethod());
			}
			throw Exceptions.wrapThrow(e);
		}
	}

	protected Processor initProcessor(String name, MvcConfig config, ActionInfo ai) throws Throwable {
		Processor p;
		if (name.startsWith("ioc:") && name.length() > 4) {
			p = config.getIoc().get(Processor.class, name.substring(4));
		}
		else {
			p = (Processor)Classes.born(name);
		}
		p.init(config, ai);
		return p;
	}

	protected void loadFromPath(String path) throws IOException {
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
	protected void loadFromStream(InputStream is) throws IOException {
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
	protected List<String> getProcessors(String key) {
		if (Strings.isEmpty(key)) {
			key = "default";
		}

		return (List<String>)map.get(key);
	}
}
