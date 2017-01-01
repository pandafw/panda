package panda.dao;

import java.sql.Types;
import java.util.HashMap;
import java.util.Map;

/**
 */
public abstract class DaoTypes {
	// ---------------------------------------------------
	// boolean
	public static final String BIT           = "BIT";
	public static final String BOOLEAN       = "BOOLEAN";

	// ---------------------------------------------------
	// string
	public static final String CHAR          = "CHAR";
	public static final String VARCHAR       = "VARCHAR";

	// ---------------------------------------------------
	// float
	public static final String FLOAT         = "FLOAT";
	public static final String DOUBLE        = "DOUBLE";
	public static final String DECIMAL       = "DECIMAL";
	public static final String NUMERIC       = "NUMERIC";
	public static final String REAL          = "REAL";

	// ---------------------------------------------------
	// integer
	public static final String TINYINT       = "TINYINT";
	public static final String SMALLINT      = "SMALLINT";
	public static final String INTEGER       = "INTEGER";
	public static final String BIGINT        = "BIGINT";

	// ---------------------------------------------------
	// date time
	public static final String DATE          = "DATE";
	public static final String TIME          = "TIME";
	public static final String TIMESTAMP     = "TIMESTAMP";

	// ---------------------------------------------------
	// clob
	public static final String CLOB          = "CLOB";
	public static final String LONGVARCHAR   = "LONGVARCHAR";

	// ---------------------------------------------------
	// blob
	public static final String BLOB          = "BLOB";
	public static final String BINARY        = "BINARY";
	public static final String VARBINARY     = "VARBINARY";
	public static final String LONGVARBINARY = "LONGVARBINARY";

	// ---------------------------------------------------
	// collection
	public static final String LIST          = "LIST";
	public static final String MAP           = "MAP";
	public static final String SET           = "SET";

	// ---------------------------------------------------
	// other
	public static final String ARRAY         = "ARRAY";
	public static final String DATALINK      = "DATALINK";
	public static final String DISTINCT      = "DISTINCT";
	public static final String JAVAOBJECT    = "JAVAOBJECT";
	public static final String NULL          = "NULL";
	public static final String OTHER         = "OTHER";
	public static final String REF           = "REF";
	public static final String STRUCT        = "STRUCT";

	// ---------------------------------------------------
	// mapping
	private static final Map<String, Integer> NAME_MAP = new HashMap<String, Integer>();
	private static final Map<Integer, String> TYPE_MAP = new HashMap<Integer, String>();

	static {
		initializeTypes();
	}

	/**
	 * set type
	 * 
	 * @param name name
	 * @param type type
	 */
	public static void setType(String name, Integer type) {
		NAME_MAP.put(name, type);
		TYPE_MAP.put(type, name);
	}

	/**
	 * Looks up a type by name, and returns it's int value (from java.sql.Types)
	 * 
	 * @param name - the type name
	 * @return - the int value (from java.sql.Types)
	 */
	public static Integer getType(String name) {
		return NAME_MAP.get(name);
	}

	/**
	 * Looks up a type by name, and returns it's int value (from java.sql.Types)
	 * 
	 * @param type - the type
	 * @return - the int value (from java.sql.Types)
	 */
	public static String getType(Integer type) {
		return TYPE_MAP.get(type);
	}

	private static void initializeTypes() {
		setType(ARRAY, Types.ARRAY);
		setType(BIGINT, Types.BIGINT);
		setType(BINARY, Types.BINARY);
		setType(BIT, Types.BIT);
		setType(BLOB, Types.BLOB);
		setType(BOOLEAN, Types.BOOLEAN);
		setType(CHAR, Types.CHAR);
		setType(CLOB, Types.CLOB);
		setType(DATALINK, Types.DATALINK);
		setType(DATE, Types.DATE);
		setType(DECIMAL, Types.DECIMAL);
		setType(DISTINCT, Types.DISTINCT);
		setType(DOUBLE, Types.DOUBLE);
		setType(FLOAT, Types.FLOAT);
		setType(INTEGER, Types.INTEGER);
		setType(JAVAOBJECT, Types.JAVA_OBJECT);
		setType(LONGVARBINARY, Types.LONGVARBINARY);
		setType(LONGVARCHAR, Types.LONGVARCHAR);
		setType(NULL, Types.NULL);
		setType(NUMERIC, Types.NUMERIC);
		setType(OTHER, Types.OTHER);
		setType(REAL, Types.REAL);
		setType(REF, Types.REF);
		setType(SMALLINT, Types.SMALLINT);
		setType(STRUCT, Types.STRUCT);
		setType(TIME, Types.TIME);
		setType(TIMESTAMP, Types.TIMESTAMP);
		setType(TINYINT, Types.TINYINT);
		setType(VARBINARY, Types.VARBINARY);
		setType(VARCHAR, Types.VARCHAR);
		
		NAME_MAP.put(LIST, Types.VARCHAR);
		NAME_MAP.put(MAP, Types.VARCHAR);
		NAME_MAP.put(SET, Types.VARCHAR);
	}
}
