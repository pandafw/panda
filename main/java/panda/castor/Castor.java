package panda.castor;


/**
 * Castor interface 
 *
 * @param <S> source type
 * @param <T> target type
 * 
 * @author yf.frank.wang@gmail.com
 */
public interface Castor<S, T> {
	/**
	 * cast value
	 * @param value value
	 * @return casted value
	 */
	T cast(S value);

	/**
	 * cast value
	 * @param context context
	 * @param value value
	 * @return casted value
	 */
	T cast(S value, CastContext context);
}
