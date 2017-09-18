package panda.lang;

import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * utility class for ClassLoader
 * 
 */
public class ClassLoaders {
	public static Set<URL> getAllClassLoaderURLs() {
		Set<URL> urls = new HashSet<URL>();
		for (ClassLoader cl : ClassLoaders.getClassLoaders()) {
			if (cl instanceof URLClassLoader) {
				for (URL url : ((URLClassLoader)cl).getURLs()) {
					File f = new File(url.getFile());
					if (f.isDirectory() && f.getParent() == null) {
						// skip root directory
						continue;
					}
					urls.add(url);
				}
			}
		}
		return urls;
	}
	
	/**
	 * @return the class loader set
	 */
	public static Set<ClassLoader> getClassLoaders() {
		return getClassLoaders(null);
	}
	
	/**
	 * @return the class loader set
	 */
	public static Set<ClassLoader> getClassLoaders(ClassLoader classLoader) {
		Set<ClassLoader> cls = new HashSet<ClassLoader>(3);

		if (classLoader != null) {
			cls.add(classLoader);
		}
		
		try {
			ClassLoader cl = Thread.currentThread().getContextClassLoader();
			if (cl != null) {
				cls.add(cl);
			}
		}
		catch (Throwable ex) {
			// Cannot access thread context ClassLoader - falling back to system class loader...
		}

		{
			ClassLoader cl = ClassLoaders.class.getClassLoader();
			if (cl != null) {
				cls.add(cl);
			}
		}
		
		try {
			ClassLoader cl = ClassLoader.getSystemClassLoader();
			if (cl != null) {
				cls.add(cl);
			}
		}
		catch (Throwable ex) {
			// AccessControlException on Google App Engine
		}

		return cls;
	}

	/**
	 * @return the class loader
	 */
	public static ClassLoader getClassLoader() {
		ClassLoader cl = null;

		try {
			cl = Thread.currentThread().getContextClassLoader();
		}
		catch (Throwable ex) {
			// Cannot access thread context ClassLoader - falling back to system class loader...
		}
		
		if (cl == null) {
			cl = ClassLoaders.class.getClassLoader();
			if (cl == null) {
				try {
					cl = ClassLoader.getSystemClassLoader();
				}
				catch (Throwable ex) {
					// skip for GAE
				}
			}
		}

		return cl;
	}

	/**
	 * Attempt to load a class.
	 * 
	 * @param name - the class to load
	 * @return the class
	 * @exception ClassNotFoundException if the class cannot be loaded
	 */
	public static Class<?> classForName(String name) throws ClassNotFoundException {
		return classForName(name, true, getClassLoaders());
	}

	/**
	 * Attempt to load a class.
	 * 
	 * @param name the class to load
	 * @param classLoader the class loader
	 * @return the class
	 * @exception ClassNotFoundException if the class cannot be loaded
	 */
	public static Class<?> classForName(String name, ClassLoader classLoader) throws ClassNotFoundException {
		return classForName(name, true, getClassLoaders(classLoader));
	}

	/**
	 * Attempt to load a class.
	 * 
	 * @param name the class to load
	 * @param initialize whether the class must be initialized
	 * @param classLoader the class loader
	 * @return the class
	 * @exception ClassNotFoundException if the class cannot be loaded
	 */
	public static Class<?> classForName(String name, boolean initialize, ClassLoader classLoader) throws ClassNotFoundException {
		return classForName(name, initialize, getClassLoaders(classLoader));
	}

	/**
	 * Load a given resource.
	 * 
	 * This method will try to load the resource.
	 * 
	 * @param resource the resource to locate
	 * @return resource URL or null
	 */
	public static URL getResourceAsURL(String resource) {
		return getResourceAsURL(resource, getClassLoaders());
	}
	
	/**
	 * Load a given resource.
	 * 
	 * @param resource the resource to locate
	 * @param classLoader The ClassLoader object of the calling object
	 * @return resource URL
	 */
	public static URL getResourceAsURL(String resource, ClassLoader classLoader) {
		return getResourceAsURL(resource, getClassLoaders(classLoader));
	}

	/**
	 * This is a convenience method to load a resource as a stream.
	 * 
	 * @param resource the resource to locate
	 * @return resource InputStream
	 */
	public static InputStream getResourceAsStream(String resource) {
		return getResourceAsStream(resource, getClassLoaders());
	}

	/**
	 * This is a convenience method to load a resource as a stream.
	 * 
	 * @param resource The name of the resource to load
	 * @param classLoader The Class object of the calling object
	 * @return resource InputStream
	 */
	public static InputStream getResourceAsStream(String resource, ClassLoader classLoader) {
		return getResourceAsStream(resource, getClassLoaders(classLoader));
	}

	/**
	 * Try to get a resource from a group of class loader
	 * 
	 * @param resource - the resource to get
	 * @param classLoaders - the class loader collection
	 * @return the resource or null
	 */
	private static InputStream getResourceAsStream(String resource, Collection<ClassLoader> classLoaders) {
		for (ClassLoader cl : classLoaders) {
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
		return null;
	}

	/**
	 * Get a resource as a URL using the current class path
	 * 
	 * @param resource - the resource to locate
	 * @param classLoaders - the class loader set to examine
	 * @return the resource or null
	 */
	private static URL getResourceAsURL(String resource, Collection<ClassLoader> classLoaders) {
		URL url;

		for (ClassLoader cl : classLoaders) {
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

		// didn't find it anywhere.
		return null;

	}

	/**
	 * Attempt to load a class from a group of class loader.
	 * This implementation supports the syntaxes "{@code java.util.Map.Entry}" and 
	 * {@code java.util.Map$Entry}".
	 * 
	 * @param name - the class to load
	 * @param initialize whether the class must be initialized
	 * @param classLoaders - the group of class loader set to examine
	 * @return the class
	 * @exception ClassNotFoundException if the class cannot be loaded
	 */
	private static Class<?> classForName(String name, boolean initialize, Collection<ClassLoader> classLoaders) throws ClassNotFoundException {
		for (ClassLoader cl : classLoaders) {
			if (cl != null) {
				try {
					Class<?> c = Class.forName(name, true, cl);
					if (null != c) {
						return c;
					}
				}
				catch (ClassNotFoundException e) {
					// we'll ignore this until all classloaders fail to locate the class
				}
			}
		}

		// allow path separators (.) as inner class name separators
		String cn = name;
		int d = 0;
		while ((d = cn.lastIndexOf('.')) != -1) {
			cn = cn.substring(0, d) + '$' + cn.substring(d + 1);
			for (ClassLoader cl : classLoaders) {
				if (cl != null) {
					try {
						Class<?> c = Class.forName(cn, true, cl);
						if (null != c) {
							return c;
						}
					}
					catch (ClassNotFoundException e) {
						// we'll ignore this until all classloaders fail to locate the class
					}
				}
			}
		}
		throw new ClassNotFoundException("Cannot find class: " + name);
	}

}
