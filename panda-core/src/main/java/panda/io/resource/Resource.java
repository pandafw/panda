package panda.io.resource;

import java.util.Enumeration;
import java.util.Locale;

public interface Resource {
	/**
	 * Returns an enumeration of the keys.
	 * 
	 * @return an <code>Enumeration</code> of the keys contained in this <code>ResourceBundle</code>
	 *         and its parent bundles.
	 */
	Enumeration<String> getKeys();

	/**
	 * Returns the locale of this resource bundle. This method can be used after a call to
	 * getBundle() to determine whether the resource bundle returned really corresponds to the
	 * requested locale or is a fallback.
	 * 
	 * @return the locale of this resource bundle
	 */
	Locale getLocale();

	/**
	 * Gets an object for the given key from this resource bundle or one of its parents. This method
	 * first tries to obtain the object from this resource bundle. If not successful, and the parent
	 * resource bundle is not null, it calls the parent's <code>getObject</code> method. If still
	 * not successful, it returns a null.
	 * 
	 * @param key the key for the desired object
	 * @exception NullPointerException if <code>key</code> is <code>null</code>
	 * @return the object for the given key
	 */
	Object getObject(String key);

	/**
	 * Gets a string for the given key from this resource bundle or one of its parents. Calling this
	 * method is equivalent to calling <blockquote>
	 * <code>(String) {@link #getObject(java.lang.String) getObject}(key)</code>. </blockquote>
	 * 
	 * @param key the key for the desired string
	 * @exception NullPointerException if <code>key</code> is <code>null</code>
	 * @exception ClassCastException if the object found for the given key is not a string
	 * @return the string for the given key
	 */
	String getString(String key);
	
	public static final Resource EMPTY = new Resource() {
		@Override
		public Enumeration<String> getKeys() {
			return null;
		}

		@Override
		public Object getObject(String key) {
			return null;
		}

		@Override
		public String getString(String key) {
			return null;
		}

		@Override
		public Locale getLocale() {
			return null;
		}
	};
}
