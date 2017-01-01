package panda.lang;

import java.io.InputStream;
import java.net.URL;

/**
 * A class to wrap access to multiple class loaders making them work as one
 * 
 */
public class ClassLoaderWrapper {
	ClassLoader defaultClassLoader;
	ClassLoader systemClassLoader;

	ClassLoaderWrapper() {
		try {
			systemClassLoader = ClassLoader.getSystemClassLoader();
		}
		catch (SecurityException ignored) {
			// AccessControlException on Google App Engine
		}
	}

	/**
	 * Get a resource as a URL using the current class path
	 * 
	 * @param resource - the resource to locate
	 * @return the resource or null
	 */
	public URL getResourceAsURL(String resource) {
		return getResourceAsURL(resource, getClassLoaders(null));
	}

	/**
	 * Get a resource from the classpath, starting with a specific class loader
	 * 
	 * @param resource - the resource to find
	 * @param callingClass - the first classloader to try
	 * @return the stream or null
	 */
	public URL getResourceAsURL(String resource, Class<?> callingClass) {
		ClassLoader classLoader = callingClass == null ? null : callingClass.getClassLoader();
		return getResourceAsURL(resource, getClassLoaders(classLoader));
	}

	/**
	 * Get a resource from the classpath, starting with a specific class loader
	 * 
	 * @param resource - the resource to find
	 * @param classLoader - the first classloader to try
	 * @return the stream or null
	 */
	public URL getResourceAsURL(String resource, ClassLoader classLoader) {
		return getResourceAsURL(resource, getClassLoaders(classLoader));
	}

	/**
	 * Get a resource from the classpath
	 * 
	 * @param resource - the resource to find
	 * @return the stream or null
	 */
	public InputStream getResourceAsStream(String resource) {
		return getResourceAsStream(resource, getClassLoaders(null));
	}

	/**
	 * Get a resource from the classpath, starting with a specific class loader
	 * 
	 * @param resource - the resource to find
	 * @param callingClass - the first class loader to try
	 * @return the stream or null
	 */
	public InputStream getResourceAsStream(String resource, Class<?> callingClass) {
		ClassLoader classLoader = callingClass == null ? null : callingClass.getClassLoader();
		return getResourceAsStream(resource, getClassLoaders(classLoader));
	}

	/**
	 * Get a resource from the classpath, starting with a specific class loader
	 * 
	 * @param resource - the resource to find
	 * @param classLoader - the first class loader to try
	 * @return the stream or null
	 */
	public InputStream getResourceAsStream(String resource, ClassLoader classLoader) {
		return getResourceAsStream(resource, getClassLoaders(classLoader));
	}

	/**
	 * Find a class on the classpath (or die trying)
	 * 
	 * @param name - the class to look for
	 * @return - the class
	 * @throws ClassNotFoundException Duh.
	 */
	public Class<?> classForName(String name) throws ClassNotFoundException {
		return classForName(name, getClassLoaders(null));
	}

	/**
	 * Find a class on the classpath, starting with a specific classloader (or die trying)
	 * 
	 * @param name - the class to look for
	 * @param callingClass - the first classloader to try
	 * @return - the class
	 * @throws ClassNotFoundException Duh.
	 */
	public Class<?> classForName(String name, Class<?> callingClass)
			throws ClassNotFoundException {
		ClassLoader classLoader = callingClass == null ? null : callingClass.getClassLoader();
		return classForName(name, getClassLoaders(classLoader));
	}

	/**
	 * Find a class on the classpath, starting with a specific classloader (or die trying)
	 * 
	 * @param name - the class to look for
	 * @param classLoader - the first classloader to try
	 * @return - the class
	 * @throws ClassNotFoundException Duh.
	 */
	public Class<?> classForName(String name, ClassLoader classLoader)
			throws ClassNotFoundException {
		return classForName(name, getClassLoaders(classLoader));
	}

	/**
	 * Try to get a resource from a group of classloaders
	 * 
	 * @param resource - the resource to get
	 * @param classLoader - the classloaders to examine
	 * @return the resource or null
	 */
	InputStream getResourceAsStream(String resource, ClassLoader[] classLoader) {
		for (ClassLoader cl : classLoader) {
			if (null != cl) {
				// try to find the resource as passed
				InputStream is = cl.getResourceAsStream(resource);

				if (null == is) {
					if (resource.charAt(0) != '/') {
						// some class loaders want this leading "/"
						is = cl.getResourceAsStream("/" + resource);
					}
					else {
						// some class loaders do not want this leading "/"
						is = cl.getResourceAsStream(resource.substring(1));
					}
				}

				if (null != is) {
					return is;
				}
			}
		}
		return null;
	}

	/**
	 * Get a resource as a URL using the current class path
	 * 
	 * @param resource - the resource to locate
	 * @param classLoader - the class loaders to examine
	 * @return the resource or null
	 */
	URL getResourceAsURL(String resource, ClassLoader[] classLoader) {
		URL url;

		for (ClassLoader cl : classLoader) {
			if (null != cl) {
				// look for the resource as passed in...
				url = cl.getResource(resource);

				if (null == url) {
					if (resource.charAt(0) != '/') {
						// some class loaders want this leading "/"
						url = cl.getResource("/" + resource);
					}
					else {
						// some class loaders do not want this leading "/"
						url = cl.getResource(resource.substring(1));
					}
				}
				
				// "It's always in the last place I look for it!"
				// ... because only an idiot would keep looking for it after finding it, so stop
				// looking already.
				if (null != url) {
					return url;
				}
			}
		}

		// didn't find it anywhere.
		return null;

	}

	/**
	 * Attempt to load a class from a group of classloaders
	 * 
	 * @param name - the class to load
	 * @param classLoader - the group of classloaders to examine
	 * @return the class
	 * @throws ClassNotFoundException - Remember the wisdom of Judge Smails: Well, the world needs
	 *             ditch diggers, too.
	 */
	Class<?> classForName(String name, ClassLoader[] classLoader) throws ClassNotFoundException {
		for (ClassLoader cl : classLoader) {
			if (null != cl) {
				try {
					Class<?> c = Class.forName(name, true, cl);
					if (null != c)
						return c;
				}
				catch (ClassNotFoundException e) {
					// we'll ignore this until all classloaders fail to locate the class
				}
			}
		}

		throw new ClassNotFoundException("Cannot find class: " + name);
	}

	ClassLoader[] getClassLoaders(ClassLoader classLoader) {
		return new ClassLoader[] { classLoader, defaultClassLoader,
				Thread.currentThread().getContextClassLoader(), 
				getClass().getClassLoader(),
				systemClassLoader };
	}
}
