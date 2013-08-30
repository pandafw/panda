package panda.castor.castors;

import panda.castor.CastContext;
import panda.castor.Castor;



/**
 * 
 * @author yf.frank.wang@gmail.com
 *
 */
public class ObjectCastor implements Castor {
	public ObjectCastor() {
	}

	/**
	 * {@inheritDoc}
	 */
	public Object cast(Object value) {
		return value;
	}

	/**
	 * {@inheritDoc}
	 */
	public Object cast(Object value, CastContext context) {
		return value;
	}
}
