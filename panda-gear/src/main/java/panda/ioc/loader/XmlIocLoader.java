package panda.ioc.loader;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.util.Arrays;
import java.util.Collection;
import java.util.Map;

import panda.bind.xml.Xmls;
import panda.io.Files;
import panda.io.Streams;
import panda.lang.Charsets;
import panda.lang.Exceptions;
import panda.log.Log;
import panda.log.Logs;

/**
 * Load Ioc configurations from Xml.
 */
public class XmlIocLoader extends MapIocLoader {
	private static final Log log = Logs.getLog(XmlIocLoader.class);

	public XmlIocLoader(Reader reader) {
		loadFromReader(reader);
		if (log.isDebugEnabled()) {
			log.debugf("Loaded %d bean define from reader --\n%s", beans.size(), beans.keySet());
		}
	}

	public XmlIocLoader(String... paths) {
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
			Collection<File> fs = Files.listFiles(p, true, "xml");
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
	
	@SuppressWarnings("unchecked")
	private void loadFromStream(InputStream is) throws IOException {
		try {
			Map<String, Object> m = (Map<String, Object>)Xmls.fromXml(is, Charsets.UTF_8, Map.class);
			if (m != null && m.size() > 0) {
				initialize(m);
			}
		}
		finally {
			Streams.safeClose(is);
		}
	}
	
	@SuppressWarnings("unchecked")
	private void loadFromReader(Reader reader) {
		Map<String, Object> m = (Map<String, Object>)Xmls.fromXml(reader, Map.class);
		if (m != null && m.size() > 0) {
			initialize(m);
		}
	}
}
