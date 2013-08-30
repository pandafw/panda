package panda.dao.sql.adapter;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import panda.castor.Castors;
import panda.dao.sql.JdbcTypes;
import panda.lang.collection.MultiKey;

/**
 * a factory class for TypeAdapter objects.
 * @author yf.frank.wang@gmail.com
 */
public class TypeAdapters {
	private static TypeAdapters me = new TypeAdapters();
	
	/**
	 * @return instance
	 */
	public static TypeAdapters me() {
		return me;
	}

	/**
	 * @return instance
	 */
	public static TypeAdapters getMe() {
		return me;
	}

	/**
	 * @param instance the instance to set
	 */
	public static void setMe(TypeAdapters instance) {
		TypeAdapters.me = instance;
	}

	//------------------------------------------------------------------
	private Castors castors = Castors.me();
	
	private Map<MultiKey, TypeAdapter<?>> adapters = new ConcurrentHashMap<MultiKey, TypeAdapter<?>>();

	/**
	 * Constructor
	 */
	public TypeAdapters() {
		TypeAdapter adapter;

		register(null, new UnknownTypeAdapter(this));

		register(Boolean.TYPE, new BooleanTypeAdapter<Boolean>(this, Boolean.TYPE));
		register(Boolean.class, new BooleanTypeAdapter<Boolean>(this, Boolean.class));
		register(Boolean.TYPE, JdbcTypes.CHAR, new BoolCharTypeAdapter.ZeroOneBoolCharTypeAdapter<Boolean>(this, Boolean.TYPE));
		register(Boolean.class, JdbcTypes.CHAR, new BoolCharTypeAdapter.ZeroOneBoolCharTypeAdapter<Boolean>(this, Boolean.class));
		register(Byte.TYPE, new ByteTypeAdapter<Byte>(this, Byte.TYPE));
		register(Byte.class, new ByteTypeAdapter<Byte>(this, Byte.class));
		register(Character.TYPE, new StringTypeAdapter<Character>(this, Character.TYPE));
		register(Character.class, new StringTypeAdapter<Character>(this, Character.class));
		register(Short.TYPE, new ShortTypeAdapter<Short>(this, Short.TYPE));
		register(Short.class, new ShortTypeAdapter<Short>(this, Short.class));
		register(Integer.TYPE, new IntegerTypeAdapter<Integer>(this, Integer.TYPE));
		register(Integer.class, new IntegerTypeAdapter<Integer>(this, Integer.class));
		register(Long.TYPE, new LongTypeAdapter<Long>(this, Long.TYPE));
		register(Long.class, new LongTypeAdapter<Long>(this, Long.class));
		register(Float.TYPE, new FloatTypeAdapter<Float>(this, Float.TYPE));
		register(Float.class, new FloatTypeAdapter<Float>(this, Float.class));
		register(Double.TYPE, new DoubleTypeAdapter<Double>(this, Double.TYPE));
		register(Double.class, new DoubleTypeAdapter<Double>(this, Double.class));
		register(String.class, new StringTypeAdapter<String>(this, String.class));
		register(StringBuilder.class, new StringTypeAdapter<StringBuilder>(this, StringBuilder.class));
		register(StringBuffer.class, new StringTypeAdapter<StringBuffer>(this, StringBuffer.class));

		adapter = new ClobTypeAdapter<String>(this, String.class);
		register(String.class, JdbcTypes.CLOB, adapter);
		register(String.class, JdbcTypes.LONGVARCHAR, adapter);

		register(BigDecimal.class, new BigDecimalAdapter<BigDecimal>(this, BigDecimal.class));

		register(byte[].class, new ByteArrayTypeAdapter<byte[]>(this, byte[].class));
		
		adapter = new BlobTypeAdapter<byte[]>(this, byte[].class);
		register(byte[].class, JdbcTypes.BLOB, adapter);
		register(byte[].class, JdbcTypes.LONGVARBINARY, adapter);

		adapter = new ObjectTypeAdapter<Object>(this, Object.class);
		register(Object.class, adapter);
		register(Object.class, JdbcTypes.JAVAOBJECT, adapter);

		adapter = new SqlTimestampTypeAdapter<Date>(this, Date.class);
		register(Date.class, adapter);
		register(Date.class, JdbcTypes.TIMESTAMP, adapter);
		register(Date.class, JdbcTypes.DATE, new SqlDateTypeAdapter<Date>(this, Date.class));
		register(Date.class, JdbcTypes.TIME, new SqlTimeTypeAdapter<Date>(this, Date.class));

		adapter = new SqlTimestampTypeAdapter<Calendar>(this, Calendar.class);
		register(Calendar.class, adapter);
		register(Calendar.class, JdbcTypes.TIMESTAMP, adapter);
		register(Calendar.class, JdbcTypes.DATE, new SqlDateTypeAdapter<Calendar>(this, Calendar.class));
		register(Calendar.class, JdbcTypes.TIME, new SqlTimeTypeAdapter<Calendar>(this, Calendar.class));

		register(java.sql.Date.class, new SqlDateTypeAdapter<java.sql.Date>(this, java.sql.Date.class));
		register(java.sql.Time.class, new SqlTimeTypeAdapter<java.sql.Time>(this, java.sql.Time.class));
		register(java.sql.Timestamp.class, new SqlTimestampTypeAdapter<java.sql.Timestamp>(this, java.sql.Timestamp.class));

		register(List.class, "LIST", new CollectionTypeAdapter(ArrayList.class));
		register(Map.class, "MAP", new CollectionTypeAdapter(LinkedHashMap.class));
		register(Set.class, "SET", new CollectionTypeAdapter(LinkedHashSet.class));
	}

	/**
	 * @return the castors
	 */
	public Castors getCastors() {
		return castors;
	}

	/**
	 * @param castors the castors to set
	 */
	public void setCastors(Castors castors) {
		this.castors = castors;
	}

	/**
	 * Get a TypeAdapter for a class
	 * 
	 * @param javaType - the class you want a TypeAdapter for
	 * 
	 * @return - the adapter
	 */
	public <T> TypeAdapter<T> getTypeAdapter(Class<T> javaType) {
		return getTypeAdapter(javaType, null);
	}

	/**
	 * Get a TypeAdapter for a class and a JDBC type
	 * 
	 * @param javaType - the java type
	 * @param jdbcType - the jdbc type
	 * 
	 * @return - the adapter
	 */
	@SuppressWarnings("unchecked")
	public <T> TypeAdapter<T> getTypeAdapter(Class<T> javaType, String jdbcType) {
		TypeAdapter<T> adapter = (TypeAdapter<T>)adapters.get(new MultiKey(javaType, jdbcType));
		if (adapter != null) {
			return adapter;
		}
		
		if (jdbcType != null) {
			adapter = (TypeAdapter<T>)adapters.get(new MultiKey(javaType, null));
			if (adapter != null) {
				return adapter;
			}
		}

		if (javaType == null) {
			// is a parameter is null, the javaType will be null
			return adapter;
		}
		
		if (javaType.isEnum()) {
			adapter = new EnumTypeAdapter(javaType);
		}
		else if (jdbcType != null) {
			Integer sqlType = JdbcTypes.getType(jdbcType);
			if (sqlType == null) {
				if ("LIST".equalsIgnoreCase(jdbcType) && List.class.isAssignableFrom(javaType)) {
					adapter = new CollectionTypeAdapter(javaType);
				}
				else if ("SET".equalsIgnoreCase(jdbcType) && Set.class.isAssignableFrom(javaType)) {
					adapter = new CollectionTypeAdapter(javaType);
				}
				else if ("MAP".equalsIgnoreCase(jdbcType) && Map.class.isAssignableFrom(javaType)) {
					adapter = new CollectionTypeAdapter(javaType);
				}
			}
			else {
				switch (sqlType) {
				case java.sql.Types.BIT:
				case java.sql.Types.BOOLEAN:
					adapter = new BooleanTypeAdapter(this, javaType);
					break;
				case java.sql.Types.TINYINT:
					adapter = new ByteTypeAdapter(this, javaType);
					break;
				case java.sql.Types.SMALLINT:
					adapter = new ShortTypeAdapter(this, javaType);
					break;
				case java.sql.Types.INTEGER:
					adapter = new IntegerTypeAdapter(this, javaType);
					break;
				case java.sql.Types.BIGINT:
					adapter = new LongTypeAdapter(this, javaType);
					break;
				case java.sql.Types.FLOAT:
					adapter = new FloatTypeAdapter(this, javaType);
					break;
				case java.sql.Types.DOUBLE:
					adapter = new DoubleTypeAdapter(this, javaType);
					break;
				case java.sql.Types.NUMERIC:
				case java.sql.Types.DECIMAL:
					adapter = new BigDecimalAdapter(this, javaType);
					break;
				case java.sql.Types.CHAR:
				case java.sql.Types.VARCHAR:
					adapter = new StringTypeAdapter(this, javaType);
					break;
				case java.sql.Types.DATE:
					adapter = new SqlDateTypeAdapter(this, javaType);
					break;
				case java.sql.Types.TIME:
					adapter = new SqlTimeTypeAdapter(this, javaType);
					break;
				case java.sql.Types.TIMESTAMP:
					adapter = new SqlTimestampTypeAdapter(this, javaType);
					break;
				case java.sql.Types.BINARY:
				case java.sql.Types.VARBINARY:
					adapter = new ByteArrayTypeAdapter(this, javaType);
					break;
				case java.sql.Types.JAVA_OBJECT:
					adapter = new ObjectTypeAdapter(this, javaType);
					break;
				case java.sql.Types.LONGVARBINARY:
				case java.sql.Types.BLOB:
					adapter = new BlobTypeAdapter(this, javaType);
					break;
				case java.sql.Types.LONGVARCHAR:
				case java.sql.Types.CLOB:
					adapter = new ClobTypeAdapter(this, javaType);
					break;
				}
			}
		}
		return adapter;
	}

	/**
	 * Register (add) a type adapter for a class and JDBC type
	 * 
	 * @param javaType - the java type
	 * @param adapter - the adapter instance
	 */
	public void register(Class<?> javaType, TypeAdapter<?> adapter) {
		register(javaType, null, adapter);
	}
	
	/**
	 * Register (add) a type adapter for a class and JDBC type
	 * 
	 * @param javaType - the java type
	 * @param jdbcType - the jdbc type
	 * @param adapter - the adapter instance
	 */
	public void register(Class<?> javaType, String jdbcType, TypeAdapter<?> adapter) {
		adapters.put(new MultiKey(javaType, jdbcType), adapter);
	}

}
