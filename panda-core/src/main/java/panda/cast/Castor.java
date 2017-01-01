package panda.cast;



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
	 * @param context context
	 * @param value value
	 * @return casted value
	 */
	T cast(S value, CastContext context);
	
	/**
	 * cast value to the provided target object,
	 * NOTE: if the target is not a mutable object, a new object will be returned.
	 * @param value value
	 * @param target target object
	 * @return casted value
	 */
	T castTo(S value, T target);

	/**
	 * cast value to the provided target object,
	 * NOTE: if the target is not a mutable object, a new object will be returned.
	 * @param value value
	 * @param target target object
	 * @param context context
	 * @return casted value
	 */
	T castTo(S value, T target, CastContext context);
}
