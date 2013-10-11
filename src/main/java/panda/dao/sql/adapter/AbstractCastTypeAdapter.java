package panda.dao.sql.adapter;

import panda.castor.Castor;


/**
 * @author yf.frank.wang@gmail.com
 */
public abstract class AbstractCastTypeAdapter<T, P> implements TypeAdapter<T> {
	private final Castor<Object, T> javaCastor;
	private final Castor<Object, P> jdbcCastor;
	
	/**
	 * @param adapters adapters
	 * @param javaType to type
	 */
	public AbstractCastTypeAdapter(TypeAdapters adapters, Class<T> javaType, Class<P> jdbcType) {
		javaCastor = adapters.getCastors().getCastor(javaType);
		jdbcCastor = adapters.getCastors().getCastor(jdbcType);
	}

	protected T castToJava(Object value) {
		return javaCastor.cast(value);
	}
	
	protected P castToJdbc(Object value) {
		return jdbcCastor.cast(value);
	}
}
