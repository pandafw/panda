package panda.io.resource;

import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

import panda.ioc.annotation.IocBean;

/**
 * A class for load external resource.
 * 
 * @author yf.frank.wang@gmail.com
 */
@IocBean
public class ResourceBundleLoader {
	private static final String JAVA_CLASS = "java.class";
	private static final String JAVA_PROPS = "java.properties";

	private Control control;
	private Map<String, ResourceBundleMaker> markers;
	private List<String> formats;

	private class Control extends ResourceBundle.Control {
		public List<String> getFormats(String baseName) {
			if (baseName == null) {
				throw new NullPointerException();
			}
			return formats;
		}

		public ResourceBundle newBundle(String baseName, Locale locale, String format, ClassLoader loader,
				boolean reload) throws IllegalAccessException, InstantiationException, IOException {

			if (baseName == null || locale == null || format == null || loader == null) {
				throw new NullPointerException();
			}

			ResourceBundleMaker rbm = markers.get(format);
			if (rbm != null) {
				return rbm.getBundle(baseName, locale, loader, reload);
			}

			return super.newBundle(baseName, locale, format, loader, reload);
		}
	}

	/**
	 * Constructor
	 */
	public ResourceBundleLoader() {
		control = new Control();
		markers = new ConcurrentHashMap<String, ResourceBundleMaker>();
		formats = new CopyOnWriteArrayList<String>();
		
		// add defaults
		addResourceBundleMaker(JAVA_CLASS, null);
		addResourceBundleMaker(JAVA_PROPS, null);
		addResourceBundleMaker(TxtResourceBundleMaker.TXT, new TxtResourceBundleMaker());
	}

	public void addResourceBundleMaker(String format, ResourceBundleMaker rbm) {
		markers.put(format, rbm);
		formats.add(format);
	}	

	public ResourceBundleMaker getResourceBundleMaker(String format) {
		return markers.get(format);
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
}
