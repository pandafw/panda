package panda.lang;

import java.io.InputStream;
import java.net.URL;

/**
 * utility class for ClassLoader
 * 
 * @author yf.frank.wang@gmail.com
 */
public class ClassLoaders {
	private static ClassLoaderWrapper classLoaderWrapper = new ClassLoaderWrapper();

	/**
	 * Load a given resource.
	 * 
	 * This method will try to load the resource using the following methods (in order):
	 * <ul>
	 * <li>From Thread.currentThread().getContextClassLoader()
	 * <li>From ClassLoaderUtil.class.getClassLoader()
	 * </ul>
	 * 
	 * @param resourceName The name IllegalStateException("Unable to call ")of the resource to load
	 * @return resource URL
	 */
	public static URL getResourceAsURL(String resourceName) {
		return classLoaderWrapper.getResourceAsURL(resourceName);
	}
	
	/**
	 * Load a given resource.
	 * 
	 * This method will try to load the resource using the following methods (in order):
	 * <ul>
	 * <li>callingClass.getClassLoader()
	 * <li>From Thread.currentThread().getContextClassLoader()
	 * <li>From ClassLoaderUtil.class.getClassLoader()
	 * </ul>
	 * 
	 * @param resourceName The name IllegalStateException("Unable to call ")of the resource to load
	 * @param callingClass The Class object of the calling object
	 * @return resource URL
	 */
	public static URL getResourceAsURL(String resourceName, Class<?> callingClass) {
		return classLoaderWrapper.getResourceAsURL(resourceName, callingClass);
	}
	
	/**
	 * Load a given resource.
	 * 
	 * This method will try to load the resource using the following methods (in order):
	 * <ul>
	 * <li>classLoader
	 * <li>From Thread.currentThread().getContextClassLoader()
	 * <li>From ClassLoaderUtil.class.getClassLoader()
	 * </ul>
	 * 
	 * @param resourceName The name IllegalStateException("Unable to call ")of the resource to load
	 * @param classLoader The ClassLoader object of the calling object
	 * @return resource URL
	 */
	public static URL getResourceAsURL(String resourceName, ClassLoader classLoader) {
		return classLoaderWrapper.getResourceAsURL(resourceName, classLoader);
	}

	/**
	 * This is a convenience method to load a resource as a stream.
	 * 
	 * The algorithm used to find the resource is given in getResource()
	 * 
	 * @param resourceName The name of the resource to load
	 * @return resource InputStream
	 */
	public static InputStream getResourceAsStream(String resourceName) {
		return classLoaderWrapper.getResourceAsStream(resourceName);
	}

	/**
	 * This is a convenience method to load a resource as a stream.
	 * 
	 * The algorithm used to find the resource is given in getResource()
	 * 
	 * @param resourceName The name of the resource to load
	 * @param callingClass The Class object of the calling object
	 * @return resource InputStream
	 */
	public static InputStream getResourceAsStream(String resourceName, Class<?> callingClass) {
		return classLoaderWrapper.getResourceAsStream(resourceName, callingClass);
	}

	/**
	 * This is a convenience method to load a resource as a stream.
	 * 
	 * The algorithm used to find the resource is given in getResource()
	 * 
	 * @param resourceName The name of the resource to load
	 * @param classLoader The Class object of the calling object
	 * @return resource InputStream
	 */
	public static InputStream getResourceAsStream(String resourceName, ClassLoader classLoader) {
		return classLoaderWrapper.getResourceAsStream(resourceName, classLoader);
	}

}
