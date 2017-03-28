package panda.io.resource;

import java.io.IOException;
import java.io.InputStream;
import java.util.Locale;
import java.util.Properties;

import panda.io.Streams;
import panda.lang.ClassLoaders;

/**
 */
public class PropResourceMaker implements ResourceMaker {
	
	public static String PROPERTIES = "properties";
	
	private String format;
	
	public PropResourceMaker() {
		this(PROPERTIES);
	}

	/**
	 * @param format property file suffix
	 */
	public PropResourceMaker(String format) {
		this.format = format;
	}

	public Resource getResource(Resource parent, String base, Locale locale, ClassLoader classLoader) throws IOException {
		final String resourceName = Resources.toResourceName(Resources.toBundleName(base, locale), format);
		InputStream is = ClassLoaders.getResourceAsStream(resourceName, classLoader);
		if (is == null) {
			return null;
		}

		try {
			Properties props = new Properties();
			props.load(is);
			
			return new MapResource(props, parent, locale);
		}
		finally {
			Streams.safeClose(is);
		}
	}
}
