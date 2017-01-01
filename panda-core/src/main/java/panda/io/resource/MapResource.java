package panda.io.resource;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;

/**
 * resource bundle for map
 */
public class MapResource extends AbstractResource {
	/**
	 * contents map
	 */
	protected final Map<String, Object> map;

	/**
	 * Constructor
	 * @param props content map
	 */
	public MapResource(Properties props, Resource parent, Locale locale) {
		super(parent, locale);

		if (props == null) {
			throw new NullPointerException();
		}
		this.map = new HashMap<String, Object>();
		for (Entry<Object, Object> en : props.entrySet()) {
			this.map.put(en.getKey().toString(), en.getValue());
		}
	}

	/**
	 * Constructor
	 * @param map content map
	 */
	public MapResource(Map<String, Object> map, Resource parent, Locale locale) {
		super(parent, locale);

		if (map == null) {
			throw new NullPointerException();
		}
		this.map = map;
	}
	
	protected Object internalGetObject(String key) {
		if (key == null) {
			throw new NullPointerException();
		}
		return map.get(key);
	}

	/**
	 * Returns a <code>Set</code> of the keys contained
	 * <em>only</em> in this <code>ResourceBundle</code>.
	 *
	 * @return a <code>Set</code> of the keys contained only in this
	 *         <code>ResourceBundle</code>
	 */
	protected Set<String> internalKeySet() {
		return map.keySet();
	}
}
