package panda.io.resource;

import java.util.Enumeration;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;

/**
 * resource bundle for map
 * @author yf.frank.wang@gmail.com
 */
public class MapResourceBundle extends ResourceBundle {
	/**
	 * contents map
	 */
	protected Map<String, Object> contents;

	/**
	 * @return the contents
	 */
	public Map<String, Object> getContents() {
		return contents;
	}

	/**
	 * @param contents the contents to set
	 */
	public void setContents(Map<String, Object> contents) {
		this.contents = contents;
	}

	/**
	 * Constructor.
	 */
	public MapResourceBundle() {
		
	}
	
	/**
	 * Constructor
	 * @param map content map
	 */
	public MapResourceBundle(Map<String, Object> map) {
		setContents(map);
	}
	
	/**
	 * Returns an <code>Enumeration</code> of the keys contained in
	 * this <code>ResourceBundle</code> and its parent bundles.
	 *
	 * @return an <code>Enumeration</code> of the keys contained in
	 *         this <code>ResourceBundle</code> and its parent bundles.
	 */
	public Enumeration<String> getKeys() {
		ResourceBundle parent = this.parent;
		return new ResourceBundleEnumeration(getContents().keySet(),
				(parent != null) ? parent.getKeys() : null);
	}

	/**
	 * @see java.util.ResourceBundle#handleGetObject(java.lang.String)
	 */
	protected Object handleGetObject(String key) {
		if (key == null) {
			throw new NullPointerException();
		}
		return getContents().get(key);
	}

	/**
	 * Returns a <code>Set</code> of the keys contained
	 * <em>only</em> in this <code>ResourceBundle</code>.
	 *
	 * @return a <code>Set</code> of the keys contained only in this
	 *         <code>ResourceBundle</code>
	 * @see #keySet()
	 */
	protected Set<String> handleKeySet() {
		return getContents().keySet();
	}
}
