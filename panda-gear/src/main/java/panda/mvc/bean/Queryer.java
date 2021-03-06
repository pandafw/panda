package panda.mvc.bean;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import panda.lang.Objects;
import panda.lang.Strings;
import panda.mvc.annotation.validate.ConstantValidate;
import panda.mvc.annotation.validate.VisitValidate;

/**
 * Query bean object
 */
public class Queryer implements Cloneable, Serializable {
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

	protected String name;
	protected Pager pager = new Pager();
	protected Sorter sorter = new Sorter();
	protected String method;
	protected Map<String, Filter> filters;

	/**
	 * constructor
	 */
	public Queryer() {
	}

	/**
	 * constructor
	 * @param method the method to set
	 */
	public Queryer(String method) {
		setMethod(method);
	}

	/**
	 * copy properties from the specified Queryer
	 * @param qr queryer
	 */
	public void copy(Queryer qr) {
		name = qr.name;
		pager = qr.pager;
		sorter = qr.sorter;
		method = qr.method;
		filters = qr.filters;
	}
	
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
	 * @return the p
	 */
	public Pager getPager() {
		return pager;
	}

	/**
	 * @param p the p to set
	 */
	public void setPager(Pager p) {
		this.pager = p;
	}

	/**
	 * @return the sorter
	 */
	public Sorter getSorter() {
		return sorter;
	}

	/**
	 * @param s the s to set
	 */
	public void setSorter(Sorter s) {
		this.sorter = s;
	}

	/**
	 * is OR
	 * @return true if method is OR
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
		if (Strings.isEmpty(method)) {
			return;
		}
		
		switch (method.charAt(0)) {
		case 'a':
		case 'A':
			this.method = AND;
			break;
		case 'o':
		case 'O':
			this.method = OR;
			break;
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
	 * @return the p
	 */
	public Pager getP() {
		return pager;
	}

	/**
	 * @param p the p to set
	 */
	public void setP(Pager p) {
		this.pager = p;
	}

	/**
	 * @return the s
	 */
	public Sorter getS() {
		return sorter;
	}

	/**
	 * @param s the s to set
	 */
	public void setS(Sorter s) {
		this.sorter = s;
	}

	/**
	 * @return the method
	 */
	@ConstantValidate(list="['and', 'or' ]")
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
	@VisitValidate
	public Map<String, Filter> getF() {
		return filters;
	}

	/**
	 * @param filters the filters to set
	 */
	public void setF(Map<String, Filter> filters) {
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
				.append("pager", pager)
				.append("sorter", sorter)
				.append("method", method)
				.append("filters", filters)
				.toString();
	}

	/**
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		return Objects.hash(name, pager, sorter, method, filters);
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

		Queryer rhs = (Queryer) obj;
		return Objects.equalsBuilder()
				.append(name, rhs.name)
				.append(pager, rhs.pager)
				.append(sorter, rhs.sorter)
				.append(method, rhs.method)
				.append(filters, rhs.filters)
				.isEquals();
	}

	/**
	 * Clone
	 * @return Clone Object
	 */
	public Queryer clone() {
		Queryer clone = new Queryer();
		
		clone.name = this.name;
		clone.pager = this.pager == null ? null : this.pager.clone();
		clone.sorter = this.sorter == null ? null : this.sorter.clone();
		clone.method = this.method;
		clone.filters = this.filters == null ? null : new HashMap<String, Filter>(this.filters);

		return clone;
	}

}
