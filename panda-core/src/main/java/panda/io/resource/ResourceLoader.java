package panda.io.resource;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

import panda.lang.ClassLoaders;
import panda.lang.Exceptions;

/**
 * A class for load external resource.
 * 
 */
public class ResourceLoader {
	private final Map<String, Resource> resources = new HashMap<String, Resource>();

	private final List<ResourceMaker> makers = new CopyOnWriteArrayList<ResourceMaker>();

	/**
	 * Constructor
	 */
	public ResourceLoader() {
		// add defaults
		addResourceMaker(new PropResourceMaker());
		addResourceMaker(new TextResourceMaker());
	}

	public void clearResourceMaker() {
		makers.clear();
	}
	
	public void addResourceMaker(ResourceMaker rbm) {
		makers.remove(rbm);
		makers.add(0, rbm);
	}

	public void clear() {
		synchronized (resources) {
			resources.clear();
		}
	}

	public Resource getResource(String base) {
		return getResource(base, Locale.getDefault());
	}

	public Resource getResource(String base, Locale locale) {
		return getResource(base, locale, ClassLoaders.getClassLoader());
	}

	public Resource getResource(String base, Locale locale, ClassLoader classLoader) {
		String key = Resources.toBundleName(base, locale);

		Resource bundle = resources.get(key);
		if (bundle == null) {
			synchronized (resources) {
				bundle = resources.get(key);
				if (bundle == null) {
					Resource parent = null;
					if (locale != null) {
						Locale parentLocale = Resources.getParentLocale(locale);
						parent = getResource(base, parentLocale, classLoader);
					}
					bundle = makeResource(parent, base, locale, classLoader);
					if (bundle == null) {
						bundle = parent == null ? Resource.EMPTY : parent;
					}
					resources.put(key, bundle);
				}
			}
		}
		return (bundle == Resource.EMPTY) ? null : bundle;
	}
	
	private Resource makeResource(Resource parent, String base, Locale locale, ClassLoader classLoader) {
		for (ResourceMaker rm : makers) {
			try {
				Resource r = rm.getResource(parent, base, locale, classLoader);
				if (r != null) {
					return r;
				}
			}
			catch (IOException e) {
				throw Exceptions.wrapThrow(e);
			}
		}
		
		return null;
	}
}
