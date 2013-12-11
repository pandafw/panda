package panda.dao.sql.adapter;

import panda.castor.Castor;


/**
 * @author yf.frank.wang@gmail.com
 *
 * @param <T> Java Type
 * @param <P> Jdbc Type
 */
public abstract class AbstractCastTypeAdapter<T, P> implements TypeAdapter<T> {
	private final Castor<P, T> javaCastor;
	private final Castor<T, P> jdbcCastor;
	
	/**
	 * @param adapters adapters
	 * @param javaType to type
	 */
	public AbstractCastTypeAdapter(TypeAdapters adapters, Class<T> javaType, Class<P> jdbcType) {
		javaCastor = adapters.getCastors().getCastor(jdbcType, javaType);
		jdbcCastor = adapters.getCastors().getCastor(javaType, jdbcType);
	}

	protected T castToJava(P value) {
		return javaCastor.cast(value);
	}
	
	protected P castToJdbc(T value) {
		return jdbcCastor.cast(value);
	}
}
