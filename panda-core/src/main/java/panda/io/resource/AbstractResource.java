package panda.io.resource;

import java.util.Enumeration;
import java.util.Locale;
import java.util.Set;

public abstract class AbstractResource implements Resource {
	/**
	 * parent
	 */
	protected final Resource parent;
	
	/**
	 * locale
	 */
	protected final Locale locale;

	/**
	 * Constructor
	 */
	protected AbstractResource(Resource parent, Locale locale) {
		this.parent = parent;
		this.locale = locale;
	}
	
	
	/**
	 * Returns an enumeration of the keys.
	 * 
	 * @return an <code>Enumeration</code> of the keys contained in this <code>ResourceBundle</code>
	 *         and its parent bundles.
	 */
	@Override
	public Enumeration<String> getKeys() {
		return new ResourceEnumeration(internalKeySet(),
				(parent != null) ? parent.getKeys() : null);
	}

	/**
	 * Returns the locale of this resource bundle. This method can be used after a call to
	 * getBundle() to determine whether the resource bundle returned really corresponds to the
	 * requested locale or is a fallback.
	 * 
	 * @return the locale of this resource bundle
	 */
	@Override
	public Locale getLocale() {
		return locale;
	}

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
	@Override
	public Object getObject(String key) {
		Object o = internalGetObject(key);
		if (o != null) {
			return o;
		}
		if (parent != null) {
			return parent.getObject(key);
		}
		return null;
	}

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
	@Override
	public String getString(String key) {
		return (String)getObject(key);
	}
	
	//-------------------------------------------
	protected abstract Object internalGetObject(String key);
	
	protected abstract Set<String> internalKeySet();
}
