package panda.util.bean;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import panda.lang.Objects;
import panda.lang.Strings;

/**
 * Query bean object
 * @author yf.frank.wang@gmail.com
 */
public class CompositeQuery implements Cloneable, Serializable {
	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * AND = "and";
	 */
	public static final String AND = "and";

	/**
	 * OR = "or";
	 */
	public static final String OR = "or";

	/**
	 * Create a AND query.
	 * @return query
	 */
	public static CompositeQuery and() {
		CompositeQuery query = new CompositeQuery();
		query.method = AND;
		return query;
	}
	
	/**
	 * Create a OR query.
	 * @return query
	 */
	public static CompositeQuery or() {
		CompositeQuery query = new CompositeQuery();
		query.method = OR;
		return query;
	}
	
	/**
	 * constructor
	 */
	public CompositeQuery() {
	}

	/**
	 * constructor
	 * @param method the method to set
	 */
	public CompositeQuery(String method) {
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
	 * is OR
	 */
	public boolean isOr() {
		return OR.equals(method);
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
		method = Strings.lowerCase(Strings.stripToNull(method));
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
	 * @return a string representation of the object.
	 */
	@Override
	public String toString() {
		return Objects.toStringBuilder()
				.append("name", name)
				.append("method", method)
				.append("filters", filters)
				.toString();
	}

	/**
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		return Objects.hashCodes(name, method, filters);
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

		CompositeQuery rhs = (CompositeQuery) obj;
		return Objects.equalsBuilder()
				.append(name, rhs.name)
				.append(method, rhs.method)
				.append(filters, rhs.filters)
				.isEquals();
	}

	/**
	 * Clone
	 * @return Clone Object
	 */
	public Object clone() {
		CompositeQuery clone = new CompositeQuery();
		
		clone.name = this.name;
		clone.method = this.method;
		clone.filters = new HashMap<String, Filter>(this.filters);

		return clone;
	}

}
