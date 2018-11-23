package panda.bind;

public interface SerializeAdapter<T> {
	public static final Object FILTERED = new Object();
	
	/**
	 * convert source
	 * @param src the source value
	 * @return converted value
	 */
	Object adaptSource(T src);

	/**
	 * @param src the owner of the property
	 * @param name the name of the property
	 * @return property name, null if the property is not accepted
	 */
	String adaptPropertyName(T src, String name);

	/**
	 * @param src the owner of the property
	 * @param value the value of the property
	 * @return property value
	 */
	Object adaptPropertyValue(T src, Object value);
}
