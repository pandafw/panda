package panda.io.resource;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.net.URLConnection;
import java.security.AccessController;
import java.security.PrivilegedActionException;
import java.security.PrivilegedExceptionAction;
import java.util.Locale;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;

import panda.io.Streams;
import panda.lang.Charsets;

/**
 * @author yf.frank.wang@gmail.com
 */
public class TxtResourceBundleMaker implements ResourceBundleMaker {
	
	public static String TXT = "txt";
	
	private String format;
	
	public TxtResourceBundleMaker() {
		this(TXT);
	}

	/**
	 * @param format
	 */
	public TxtResourceBundleMaker(String format) {
		this.format = format;
	}

	public ResourceBundle getBundle(String baseName, Locale locale, ClassLoader loader,
			boolean reload) throws IllegalAccessException, InstantiationException, IOException {
		
		final String resourceName = Resources.toResourceName(Resources.toBundleName(baseName, locale), format);
		final ClassLoader classLoader = loader;
		final boolean reloadFlag = reload;
		Reader reader = null;
		try {
			reader = AccessController.doPrivileged(new PrivilegedExceptionAction<Reader>() {
				public Reader run() throws IOException {
					InputStream is = null;
					if (reloadFlag) {
						URL url = classLoader.getResource(resourceName);
						if (url != null) {
							URLConnection connection = url.openConnection();
							if (connection != null) {
								// Disable caches to get fresh data for
								// reloading.
								connection.setUseCaches(false);
								is = connection.getInputStream();
							}
						}
					}
					else {
						is = classLoader.getResourceAsStream(resourceName);
					}
					if (is == null) {
						return null;
					}
					return new InputStreamReader(is, Charsets.UTF_8);
				}
			});
		}
		catch (PrivilegedActionException e) {
			throw (IOException)e.getException();
		}
		if (reader != null) {
			try {
				return new PropertyResourceBundle(reader);
			}
			finally {
				Streams.safeClose(reader);
			}
		}
		return null;
	}
}
