package panda.dao.query;

import panda.lang.Asserts;



/**
 */
public class Join {
	public static final String LEFT = "LEFT";
	public static final String RIGHT = "RIGHT";
	public static final String INNER = "INNER";
	
	private String type;
	private Query<?> query;
	private String[] conditions;

	/**
	 * @param query join query
	 * @param conditions join conditions
	 */
	public Join(Query<?> query, String[] conditions) {
		this(null, query, conditions);
	}

	/**
	 * @param type join type
	 * @param query join query
	 * @param conditions join conditions
	 */
	public Join(String type, Query<?> query, String[] conditions) {
		Asserts.notNull(query, "The parameter query is null");
		Asserts.notEmpty(conditions, "The parameter conditions is empty");
		
		this.type = type;
		this.query = query;
		this.conditions = conditions;
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
	 * @return the query
	 */
	public Query<?> getQuery() {
		return query;
	}

	/**
	 * @param query the query to set
	 */
	public void setQuery(Query<?> query) {
		this.query = query;
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
}

