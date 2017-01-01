package panda.bind;

/**
 * 
 * @author yf.frank.wang@gmail.com
 *
 * @param <T> class type
 */
public interface PropertyFilter<T> {
	/**
	 * @param source the owner of the property
	 * @param name the name of the property
	 * @param value the value of the property
	 * @return true if the property is needed
	 */
	boolean accept(Object source, String name, Object value);
}
