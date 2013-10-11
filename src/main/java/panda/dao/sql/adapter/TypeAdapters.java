package panda.dao.sql.adapter;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
	/**
	 * casters used for cast value
	 */
	private Castors castors = Castors.me();
	
	/**
	 * all adapters should be registered before use
	 */
	private Map<MultiKey, TypeAdapter<?>> adapters = new HashMap<MultiKey, TypeAdapter<?>>();

	/**
	 * Constructor
	 */
	public TypeAdapters() {
		TypeAdapter adapter;

		//----------------------------------------------------
		// boolean
		//
		register(Boolean.TYPE, new BooleanTypeAdapter<Boolean>(this, Boolean.TYPE));
		register(Boolean.TYPE, JdbcTypes.TINYINT, new ByteTypeAdapter<Boolean>(this, Boolean.TYPE));
		register(Boolean.TYPE, JdbcTypes.SMALLINT, new ShortTypeAdapter<Boolean>(this, Boolean.TYPE));
		register(Boolean.TYPE, JdbcTypes.INTEGER, new IntegerTypeAdapter<Boolean>(this, Boolean.TYPE));
		register(Boolean.TYPE, JdbcTypes.BIGINT, new LongTypeAdapter<Boolean>(this, Boolean.TYPE));

		adapter = new BooleanTypeAdapter<Boolean>(this, Boolean.class);
		register(Boolean.class, adapter);
		register(JdbcTypes.BOOLEAN, adapter);
		register(Boolean.class, JdbcTypes.TINYINT, new ByteTypeAdapter<Boolean>(this, Boolean.class));
		register(Boolean.class, JdbcTypes.SMALLINT, new ShortTypeAdapter<Boolean>(this, Boolean.class));
		register(Boolean.class, JdbcTypes.INTEGER, new IntegerTypeAdapter<Boolean>(this, Boolean.class));
		register(Boolean.class, JdbcTypes.BIGINT, new LongTypeAdapter<Boolean>(this, Boolean.class));

		register(Boolean.TYPE, JdbcTypes.CHAR, new BoolCharTypeAdapter.ZeroOneBoolCharTypeAdapter<Boolean>(this, Boolean.TYPE));
		adapter = new BoolCharTypeAdapter.ZeroOneBoolCharTypeAdapter<Boolean>(this, Boolean.class);
		register(Boolean.class, JdbcTypes.CHAR, adapter);
		register(JdbcTypes.CHAR, adapter);

		//----------------------------------------------------
		// integer
		//
		register(Byte.TYPE, new ByteTypeAdapter<Byte>(this, Byte.TYPE));
		register(Byte.class, new ByteTypeAdapter<Byte>(this, Byte.class));
		
		register(Short.TYPE, new ShortTypeAdapter<Short>(this, Short.TYPE));
		register(Short.class, new ShortTypeAdapter<Short>(this, Short.class));

		register(Integer.TYPE, new IntegerTypeAdapter<Integer>(this, Integer.TYPE));
		register(Integer.class, new IntegerTypeAdapter<Integer>(this, Integer.class));

		register(Long.TYPE, new LongTypeAdapter<Long>(this, Long.TYPE));
		adapter = new LongTypeAdapter<Long>(this, Long.class);
		register(Long.class, adapter);
		register(JdbcTypes.TINYINT, adapter);
		register(JdbcTypes.SMALLINT, adapter);
		register(JdbcTypes.INTEGER, adapter);
		register(JdbcTypes.BIGINT, adapter);

		//----------------------------------------------------
		// float
		//
		register(Float.TYPE, new FloatTypeAdapter<Float>(this, Float.TYPE));
		register(Float.class, new FloatTypeAdapter<Float>(this, Float.class));

		register(Double.TYPE, new DoubleTypeAdapter<Double>(this, Double.TYPE));
		adapter = new DoubleTypeAdapter<Double>(this, Double.class);
		register(Double.class, adapter);
		register(JdbcTypes.FLOAT, adapter);
		register(JdbcTypes.DOUBLE, adapter);
		register(JdbcTypes.REAL, adapter);

		adapter = new BigDecimalAdapter<BigDecimal>(this, BigDecimal.class);
		register(BigDecimal.class, adapter);
		register(JdbcTypes.DECIMAL, adapter);
		register(JdbcTypes.NUMERIC, adapter);

		//----------------------------------------------------
		// string
		//
		register(Character.TYPE, new StringTypeAdapter<Character>(this, Character.TYPE));
		register(Character.class, new StringTypeAdapter<Character>(this, Character.class));

		adapter = new StringTypeAdapter<String>(this, String.class);
		register(String.class, adapter);
		register(JdbcTypes.CHAR, adapter);
		register(JdbcTypes.VARCHAR, adapter);

		register(StringBuilder.class, new StringTypeAdapter<StringBuilder>(this, StringBuilder.class));
		register(StringBuffer.class, new StringTypeAdapter<StringBuffer>(this, StringBuffer.class));

		adapter = new ClobTypeAdapter<String>(this, String.class);
		register(JdbcTypes.CLOB, adapter);
		register(JdbcTypes.LONGVARCHAR, adapter);

		
		//----------------------------------------------------
		// binary
		//
		register(byte[].class, new ByteArrayTypeAdapter<byte[]>(this, byte[].class));

		adapter = new BlobTypeAdapter<byte[]>(this, byte[].class);
		register(byte[].class, JdbcTypes.BLOB, adapter);
		register(byte[].class, JdbcTypes.LONGVARBINARY, adapter);

		//----------------------------------------------------
		// date time
		//
		adapter = new LongTypeAdapter<Date>(this, Date.class);
		register(Date.class, JdbcTypes.INTEGER, adapter);
		register(Date.class, JdbcTypes.BIGINT, adapter);
		register(Date.class, JdbcTypes.DATE, new SqlDateTypeAdapter<Date>(this, Date.class));
		register(Date.class, JdbcTypes.TIME, new SqlTimeTypeAdapter<Date>(this, Date.class));
		adapter = new SqlTimestampTypeAdapter<Date>(this, Date.class);
		register(Date.class, JdbcTypes.TIMESTAMP, adapter);
		register(Date.class, adapter);

		adapter = new LongTypeAdapter<Calendar>(this, Calendar.class);
		register(Calendar.class, JdbcTypes.INTEGER, adapter);
		register(Calendar.class, JdbcTypes.BIGINT, adapter);
		register(Calendar.class, JdbcTypes.DATE, new SqlDateTypeAdapter<Calendar>(this, Calendar.class));
		register(Calendar.class, JdbcTypes.TIME, new SqlTimeTypeAdapter<Calendar>(this, Calendar.class));
		adapter = new SqlTimestampTypeAdapter<Calendar>(this, Calendar.class);
		register(Calendar.class, JdbcTypes.TIMESTAMP, adapter);
		register(Calendar.class, adapter);

		adapter = new LongTypeAdapter<GregorianCalendar>(this, GregorianCalendar.class);
		register(GregorianCalendar.class, JdbcTypes.INTEGER, adapter);
		register(GregorianCalendar.class, JdbcTypes.BIGINT, adapter);
		register(GregorianCalendar.class, JdbcTypes.DATE, new SqlDateTypeAdapter<GregorianCalendar>(this, GregorianCalendar.class));
		register(GregorianCalendar.class, JdbcTypes.TIME, new SqlTimeTypeAdapter<GregorianCalendar>(this, GregorianCalendar.class));
		adapter = new SqlTimestampTypeAdapter<GregorianCalendar>(this, GregorianCalendar.class);
		register(GregorianCalendar.class, JdbcTypes.TIMESTAMP, adapter);
		register(GregorianCalendar.class, adapter);

		adapter = new SqlDateTypeAdapter<java.sql.Date>(this, java.sql.Date.class);
		register(java.sql.Date.class, adapter);
		register(JdbcTypes.DATE, adapter);

		adapter = new SqlTimeTypeAdapter<java.sql.Time>(this, java.sql.Time.class);
		register(java.sql.Time.class, adapter);
		register(JdbcTypes.TIME, adapter);
		
		adapter = new SqlTimestampTypeAdapter<java.sql.Timestamp>(this, java.sql.Timestamp.class);
		register(java.sql.Timestamp.class, adapter);
		register(JdbcTypes.TIMESTAMP, adapter);

		//----------------------------------------------------
		// binary
		//
		adapter = new ObjectTypeAdapter<Object, Object>(this, Object.class, Object.class);
		register(Object.class, adapter);
		register(Object.class, JdbcTypes.JAVAOBJECT, adapter);

		//----------------------------------------------------
		// json collection
		//
		register("LIST", new CollectionTypeAdapter(ArrayList.class));
		register("MAP", new CollectionTypeAdapter(LinkedHashMap.class));
		register("SET", new CollectionTypeAdapter(LinkedHashSet.class));
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
		if (javaType == null) {
			// null javaType is only used for setParameter
			javaType = (Class<T>)Object.class;
		}
		
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
					adapter = new ObjectTypeAdapter(this, javaType, Object.class);
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
	 * Register (add) a type adapter for a class
	 * 
	 * @param javaType - the java type
	 * @param adapter - the adapter instance
	 */
	public void register(Class<?> javaType, TypeAdapter<?> adapter) {
		register(javaType, null, adapter);
	}

	/**
	 * Register (add) a type adapter for a JDBC type
	 * 
	 * @param jdbcType - the jdbc type
	 * @param adapter - the adapter instance
	 */
	public void register(String jdbcType, TypeAdapter<?> adapter) {
		register(Object.class, jdbcType, adapter);
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
