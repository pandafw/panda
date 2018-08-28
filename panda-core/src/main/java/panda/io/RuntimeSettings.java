package panda.io;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import panda.lang.Collections;
import panda.lang.time.DateTimes;
import panda.log.Log;
import panda.log.Logs;

public class RuntimeSettings extends Settings {

	private static final Log log = Logs.getLog(RuntimeSettings.class);
	
	protected static class RuntimeFile {
		/**
		 * file
		 */
		private File file;

		/**
		 * load time
		 */
		private long load;

		/**
		 * @param file the reloadable setting file
		 */
		public RuntimeFile(File file) {
			this.file = file;
		}
	}

	private List<RuntimeFile> runtimes = new ArrayList<RuntimeFile>();
	
	/**
	 * last check time
	 */
	private long checkpoint = 0;
	
	/**
	 * check delay (default: 60s)
	 */
	private long delay = 60000;

	public RuntimeSettings() {
	}

	public RuntimeSettings(String file) throws IOException {
		load(file);
	}

	/**
	 * @return the delay
	 */
	public long getDelay() {
		return delay;
	}

	/**
	 * @param delay the delay to set
	 */
	public void setDelay(long delay) {
		this.delay = delay;
	}

	protected void reload() {
		if (Collections.isEmpty(runtimes) || checkpoint <= 0) {
			return;
		}
		
		long now = System.currentTimeMillis();
		if (now < checkpoint) {
			return;
		}
		
		synchronized (this) {
			if (now < checkpoint) {
				return;
			}
			checkpoint = now + delay;

			for (RuntimeFile rf : runtimes) {
				long lm = rf.file.lastModified();
				if (lm != rf.load) {
					log.info("Runtime setting file " + rf.file + " is modified at " + DateTimes.isoDatetimeFormat().format(lm));
					loadRuntimeFile(rf);
				}
			}
		}
	}
	
	public void loadRuntimes(String... files) {
		if (files == null) {
			return;
		}
		
		for (String file : files) {
			loadRuntime(new File(file));
		}
	}
	
	public void loadRuntime(File file) {
		this.checkpoint = System.currentTimeMillis() + delay;
		
		RuntimeFile rf = new RuntimeFile(file);
		runtimes.add(rf);
		loadRuntimeFile(rf);
	}

	protected void loadRuntimeFile(RuntimeFile rf) {
		rf.load = rf.file.lastModified();
		if (rf.file.exists()) {
			log.info("Loading runtime setting file: " + rf.file);
			try {
				load(rf.file);
			}
			catch (IOException e) {
				log.error("Failed to load runtime setting file: " + rf.file);
			}
		}
		else {
			log.warn("Missing runtime setting file: " + rf.file);
		}
	}

	//-------------------------------------------------------
	@Override
	public int size() {
		reload();
		return super.size();
	}

	@Override
	public boolean isEmpty() {
		reload();
		return super.isEmpty();
	}

	@Override
	public boolean containsKey(Object key) {
		reload();
		return super.containsKey(key);
	}

	@Override
	public boolean containsValue(Object value) {
		reload();
		return super.containsValue(value);
	}

	@Override
	public String get(Object key) {
		reload();
		return super.get(key);
	}

	@Override
	public Set<String> keySet() {
		reload();
		return super.keySet();
	}

	@Override
	public Collection<String> values() {
		reload();
		return super.values();
	}

	@Override
	public Set<java.util.Map.Entry<String, String>> entrySet() {
		reload();
		return super.entrySet();
	}
}
