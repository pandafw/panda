package panda.resource;

import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

import panda.lang.Arrays;
import panda.lang.Collections;

/**
 * A class for load external resource.
 * @author yf.frank.wang@gmail.com
 */
public class ExternalResourceLoader16 extends ExternalResourceLoader {
	private Control control;
	
	/**
	 * Constructor
	 */
	public ExternalResourceLoader16() {
		control = new Control(this);
	}

	public ResourceBundle getBundle(String baseName) {
		return ResourceBundle.getBundle(baseName, control);
	}

	public ResourceBundle getBundle(String baseName, Locale locale) {
		return ResourceBundle.getBundle(baseName, locale, control);
	}

	public ResourceBundle getBundle(String baseName, Locale locale, ClassLoader loader) {
		return ResourceBundle.getBundle(baseName, locale, loader, control);
	}

	public static class Control extends ResourceBundle.Control {
		private static final String PANDA_EXT = "panda.ext";
		/**
		 * The default format <code>List</code>, which contains the strings
		 * <code>"panda.ext"</code> and <code>"java.class"</code> and <code>"java.properties"</code>, in
		 * this order. This <code>List</code> is {@linkplain
		 * Collections#unmodifiableList(List) unmodifiable}.
		 */
		public static final List<String> FORMAT_DEFAULT = Collections.unmodifiableList(Arrays.asList(PANDA_EXT,
			"java.class", "java.properties"));

		private ExternalResourceLoader externalResourceLoader;
		
		public Control(ExternalResourceLoader externalResourceLoader) {
			this.externalResourceLoader = externalResourceLoader;
		}
		
		public List<String> getFormats(String baseName) {
			if (baseName == null) {
				throw new NullPointerException();
			}
			return FORMAT_DEFAULT;
		}

		@SuppressWarnings("unchecked")
		public ResourceBundle newBundle(String baseName, Locale locale, String format,
				ClassLoader loader, boolean reload) throws IllegalAccessException,
				InstantiationException, IOException {
			if (baseName == null || locale == null || format == null || loader == null) {
				throw new NullPointerException();
			}

			if (PANDA_EXT.equals(format)) {
				Map contents = externalResourceLoader.getContentsMap(toBundleName(baseName, locale));
				if (contents != null) {
					MapResourceBundle bundle = new MapResourceBundle(contents);
					return bundle;
				}
			}
			
			return super.newBundle(baseName, locale, format, loader, reload);
		}
	}
}
