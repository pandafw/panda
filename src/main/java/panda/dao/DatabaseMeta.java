package panda.dao;

/**
 * @author yf.frank.wang@gmail.com
 */
public class DatabaseMeta {
	/**
	 * db type
	 */
	private DB type;

	/**
	 * name
	 */
	private String name;

	/**
	 * version
	 */
	private String version;

	/**
	 * Constructor
	 * @param type database type
	 */
	public DatabaseMeta(DB type) {
		this.type = type;
	}

	/**
	 * Constructor
	 * @param type database type
	 * @param name product name
	 * @param version version
	 */
	public DatabaseMeta(DB type, String name, String version) {
		this.type = type;
		this.name = name;
		this.version = version;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public DB getType() {
		return type;
	}

	public String getTypeName() {
		return type.name();
	}

	public boolean isGeneral() {
		return DB.GENERAL == type;
	}

	public boolean isMySql() {
		return DB.MYSQL == type;
	}

	public boolean isPostgreSql() {
		return DB.POSTGRE == type;
	}

	public boolean isSqlServer() {
		return DB.MSSQL == type;
	}

	public boolean isOracle() {
		return DB.ORACLE == type;
	}

	public boolean isDB2() {
		return DB.DB2 == type;
	}

	public boolean isH2() {
		return DB.H2 == type;
	}

	public boolean isSQLite() {
		return DB.SQLITE == type;
	}

	public boolean isHsql() {
		return DB.HSQLDB == type;
	}

	public boolean isDerby() {
		return DB.DERBY == type;
	}

	public boolean isGae() {
		return DB.GAE == type;
	}

	public boolean isRDBMS() {
		return DB.GAE != type;
	}
	
	@Override
	public String toString() {
		return String.format("%s:[%s - %s]", type.name(), name, version);
	}
}
