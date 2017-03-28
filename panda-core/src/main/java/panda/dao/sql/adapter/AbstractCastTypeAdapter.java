package panda.dao.sql.adapter;

import panda.cast.Castor;
import panda.cast.Castors;


/**
 * @param <T> Java Type
 * @param <P> Jdbc Type
 */
public abstract class AbstractCastTypeAdapter<T, P> implements TypeAdapter<T> {
	private final Castors castors;
	private final Castor<P, T> javaCastor;
	private final Castor<T, P> jdbcCastor;
	
	/**
	 * @param adapters adapters
	 * @param javaType the java type
	 * @param jdbcType the jdbc type
	 */
	public AbstractCastTypeAdapter(TypeAdapters adapters, Class<T> javaType, Class<P> jdbcType) {
		castors = adapters.getCastors();
		javaCastor = castors.getCastor(jdbcType, javaType);
		jdbcCastor = castors.getCastor(javaType, jdbcType);
	}

	protected T castToJava(P value) {
		return javaCastor.cast(value, castors.newCastContext());
	}
	
	protected P castToJdbc(T value) {
		return jdbcCastor.cast(value, castors.newCastContext());
	}
}
