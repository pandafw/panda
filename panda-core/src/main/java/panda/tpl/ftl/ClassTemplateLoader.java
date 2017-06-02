package panda.tpl.ftl;

import java.net.URL;

import panda.lang.ClassLoaders;
import freemarker.cache.URLTemplateLoader;

/**
 * ClassTemplateLoader
 */
public class ClassTemplateLoader extends URLTemplateLoader {

	private String path;

	/**
	 * Constructor
	 */
	public ClassTemplateLoader() {
		this("/");
	}

	/**
	 * Constructor
	 * @param path the base path to template resources. 
	 */
	public ClassTemplateLoader(String path) {
		setPath(path);
	}

	private void setPath(String path) {
		if (path == null) {
			throw new IllegalArgumentException("path == null");
		}
		this.path = canonicalizePrefix(path);
	}

	protected URL getURL(String name) {
		if (name.startsWith("/")) {
			name = name.substring(1);
		}
		return ClassLoaders.getResourceAsURL(path + name, getClass().getClassLoader());
	}
}
