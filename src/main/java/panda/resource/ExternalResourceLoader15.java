package panda.resource;

import panda.lang.Classes;
import panda.lang.Strings;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;


/**
 * A class for load external resource.
 * @author yf.frank.wang@gmail.com
 */
public class ExternalResourceLoader15 extends ExternalResourceLoader {

	private PropertiesLoader propertiesLoader = new PropertiesLoader(this);

	/**
	 * @return the classLoader
	 */
	public ClassLoader getClassLoader() {
		return propertiesLoader;
	}

	/**
	 * @return the propertiesLoader
	 */
	public PropertiesLoader getPropertiesLoader() {
		return propertiesLoader;
	}

	/**
	 * A class loader for load external properties
	 */
	public static class PropertiesLoader extends ClassLoader {
		private ExternalResourceLoader externalResourceLoader;
		
		/**
		 * @param externalResourceLoader external resource loader
		 */
		public PropertiesLoader(ExternalResourceLoader15 externalResourceLoader) {
			this.externalResourceLoader = externalResourceLoader;
		}
		
		/**
		 * @see java.lang.ClassLoader#loadClass(java.lang.String)
		 */
		public Class<?> loadClass(String name) throws ClassNotFoundException {
			return null;
		}
	
		/**
		 * @param name resource name
		 * @return null
		 */
		public URL getResource(String name) {
			return null;
		}
	
		/**
		 * load this resource from external
		 */
		public InputStream getResourceAsStream(String name) {
			String basename = Strings.substringBeforeLast(name, ".").replace('/', '.');

			Map<String, String> m = externalResourceLoader.getContentsMap(basename);
			if (m != null) {
				if (log.isDebugEnabled()) {
					log.debug("Load resource - " + basename + " ... " + (m == null ? "-" : m.size()));
				}

				try {
					Properties p = new Properties();
					for (Entry<String, String> e : m.entrySet()) {
						String key = e.getKey();
						String val = e.getValue();
						if (key != null) {
							p.put(key, val == null ? Strings.EMPTY : val);
						}
					}
					
					ByteArrayOutputStream baos = new ByteArrayOutputStream();
					p.store(baos, null);
					
					return new ByteArrayInputStream(baos.toByteArray());
				}
				catch (Throwable e) {
					log.error("properties load failed - " + name, e);
					throw new RuntimeException("properties load failed - " + name, e);
				}
			}
			
			return Classes.getClassLoader().getResourceAsStream(name);
		}
	}
}
