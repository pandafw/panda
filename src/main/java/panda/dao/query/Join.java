package panda.dao.query;

import panda.lang.Asserts;



/**
 * @author yf.frank.wang@gmail.com
 */
public class Join {
	public static final String LEFT = "LEFT";
	public static final String RIGHT = "RIGHT";
	public static final String INNER = "INNER";
	
	private String type;
	private String table;
	private String alias;
	private String[] conditions;
	private Object[] parameters;

	/**
	 * @param table table name
	 * @param alias table alias
	 * @param conditions join conditions
	 */
	public Join(String table, String alias, String[] conditions) {
		this(null, table, alias, conditions);
	}

	/**
	 * @param type join type
	 * @param table table name
	 * @param alias table alias
	 * @param conditions join conditions
	 */
	public Join(String type, String table, String alias, String[] conditions) {
		this(type, table, alias, conditions, null);
	}

	/**
	 * @param type join type
	 * @param table table name
	 * @param alias table alias
	 * @param conditions join conditions
	 * @param parameters parameters
	 */
	public Join(String type, String table, String alias, String[] conditions, Object[] parameters) {
		Asserts.notEmpty(table, "The parameter table is empty");
		Asserts.notEmpty(alias, "The parameter alias is empty");
		Asserts.notEmpty(conditions, "The parameter conditions is empty");
		
		this.type = type;
		this.table = table;
		this.alias = alias;
		this.conditions = conditions;
		this.parameters = parameters;
	}

	/**
	 * @return the type
	 */
	public String getType() {
		return type;
	}

	/**
	 * @param type the type to set
	 */
	public void setType(String type) {
		this.type = type;
	}

	/**
	 * @return the table
	 */
	public String getTable() {
		return table;
	}

	/**
	 * @param table the table to set
	 */
	public void setTable(String table) {
		this.table = table;
	}

	/**
	 * @return the alias
	 */
	public String getAlias() {
		return alias;
	}

	/**
	 * @param alias the alias to set
	 */
	public void setAlias(String alias) {
		this.alias = alias;
	}

	/**
	 * @return the conditions
	 */
	public String[] getConditions() {
		return conditions;
	}

	/**
	 * @param conditions the conditions to set
	 */
	public void setConditions(String[] conditions) {
		this.conditions = conditions;
	}

	/**
	 * @return the parameters
	 */
	public Object[] getParameters() {
		return parameters;
	}

	/**
	 * @param parameters the parameters to set
	 */
	public void setParameters(Object[] parameters) {
		this.parameters = parameters;
	}

}

