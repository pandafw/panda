package panda.bind;

/**
 * 
 * @author yf.frank.wang@gmail.com
 *
 * @param <T> class type
 */
public interface SourceAdapter<T> {
	public static final int ADAPTER_NONE = 0;
	public static final int ADAPTER_AUTO = 1;
	public static final int ADAPTER_STRING = 2;
	public static final int ADAPTER_NUMBER = 3;
	public static final int ADAPTER_BOOLEAN = 4;
	
	/**
	 * @param source the owner of the property
	 * @param name the name of the property
	 * @param value the value of the property
	 * @return adapter type
	 */
	Object apply(T source);
}
