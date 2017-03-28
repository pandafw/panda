package panda.io.resource;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Locale;
import java.util.Properties;

import panda.io.Streams;
import panda.lang.Charsets;
import panda.lang.ClassLoaders;

/**
 */
public class TextResourceMaker implements ResourceMaker {
	
	public static String TXT = "txt";
	
	private String format;
	
	public TextResourceMaker() {
		this(TXT);
	}

	/**
	 * @param format resource file suffix
	 */
	public TextResourceMaker(String format) {
		this.format = format;
	}

	public Resource getResource(Resource parent, String base, Locale locale, ClassLoader classLoader) throws IOException {
		final String resourceName = Resources.toResourceName(Resources.toBundleName(base, locale), format);
		InputStream is = ClassLoaders.getResourceAsStream(resourceName, classLoader);
		if (is == null) {
			return null;
		}

		Reader reader = null;
		try {
			reader = new InputStreamReader(is, Charsets.UTF_8);

			Properties props = new Properties();
			props.load(reader);
			
			return new MapResource(props, parent, locale);
		}
		finally {
			Streams.safeClose(reader);
		}
	}
}
