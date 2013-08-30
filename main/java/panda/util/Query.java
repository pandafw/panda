package panda.util;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import panda.dao.Conditions;
import panda.lang.Strings;

/**
 * Query bean object
 * @author yf.frank.wang@gmail.com
 */
@SuppressWarnings("serial")
public class Query implements Cloneable, Serializable {

	/**
	 * AND = "and";
	 */
	public static final String AND = Conditions.AND.toLowerCase();

	/**
	 * OR = "or";
	 */
	public static final String OR = Conditions.OR.toLowerCase();

	/**
	 * Create a AND query.
	 * @return query
	 */
	public static Query and() {
		Query query = new Query();
		query.method = AND;
		return query;
	}
	
	/**
	 * Create a OR query.
	 * @return query
	 */
	public static Query or() {
		Query query = new Query();
		query.method = OR;
		return query;
	}
	
	/**
	 * constructor
	 */
	public Query() {
	}

	/**
	 * constructor
	 * @param method the method to set
	 */
	public Query(String method) {
		setMethod(method);
	}

	private String name;
	private String method = AND;
	private Map<String, Filter> filters;

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = Strings.stripToNull(name);
	}

	/**
	 * @return the method
	 */
	public String getMethod() {
		return method;
	}

	/**
	 * @param method the method to set
	 */
	public void setMethod(String method) {
		method = Strings.stripToNull(method);
		if (method != null) {
			this.method = Strings.lowerCase(method);
		}
	}

	/**
	 * @return the filters
	 */
	public Map<String, Filter> getFilters() {
		return filters;
	}

	/**
	 * @param filters the filters to set
	 */
	public void setFilters(Map<String, Filter> filters) {
		this.filters = filters;
	}

	//-------------------------------------------------------------
	// short name
	//-------------------------------------------------------------
	/**
	 * @return the name
	 */
	public String getN() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setN(String name) {
		this.name = name;
	}

	/**
	 * @return the method
	 */
	public String getM() {
		return method;
	}

	/**
	 * @param method the method to set
	 */
	public void setM(String method) {
		setMethod(method);
	}

	/**
	 * @return the filters
	 */
	public Map<String, Filter> getFs() {
		return filters;
	}

	/**
	 * @param filters the filters to set
	 */
	public void setFs(Map<String, Filter> filters) {
		this.filters = filters;
	}

	/**
	 * normalize
	 */
	public void normalize() {
		Filter.normalize(filters);
	}

	/**
     * @return  a string representation of the object.
	 */
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();

		sb.append("{ ");
		sb.append("name: ").append(name);
		sb.append(", ");
		sb.append("method: ").append(method);
		sb.append(", ");
		sb.append("filters: ").append(filters);
		sb.append(" }");
		
		return sb.toString();
	}

	/**
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((method == null) ? 0 : method.hashCode());
		result = prime * result + ((filters == null) ? 0 : filters.hashCode());
		return result;
	}

	/**
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Query other = (Query) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		}
		else if (!name.equals(other.name))
			return false;
		if (method == null) {
			if (other.method != null)
				return false;
		}
		else if (!method.equals(other.method))
			return false;
		if (filters == null) {
			if (other.filters != null)
				return false;
		}
		else if (!filters.equals(other.filters))
			return false;
		return true;
	}

	/**
	 * Clone
	 * @return Clone Object
	 */
	public Object clone() {
		Query clone = new Query();
		
		clone.name = this.name;
		clone.method = this.method;
		clone.filters = new HashMap<String, Filter>(this.filters);

		return clone;
	}

}
