package panda.dao.sql.criteria;

import panda.lang.Objects;


/**
 * @author yf.frank.wang@gmail.com
 */
public class SqlUpdateParameter extends SqlQueryParameter {

	private Object data;

	/**
	 * @return data
	 */
	public Object getData() {
		return data;
	}

	/**
	 * @param data the data to set
	 */
	public void setData(Object data) {
		this.data = data;
	}

	/**
	 * constructor
	 */
	public SqlUpdateParameter() {
		super();
	}

	/**
	 * constructor
	 * 
	 * @param qp QueryParameter
	 */
	public SqlUpdateParameter(SqlQueryParameter qp) {
		super(qp);
	}

	/**
	 * constructor
	 * 
	 * @param up UpdateParameter
	 */
	public SqlUpdateParameter(SqlUpdateParameter up) {
		super(up);
		this.data = up.data;
	}

	/**
	 * constructor
	 * 
	 * @param data data
	 */
	public SqlUpdateParameter(Object data) {
		super();
		this.data = data;
	}

	/**
	 * constructor
	 * 
	 * @param data data
	 * @param qp QueryParameter
	 */
	public SqlUpdateParameter(Object data, SqlQueryParameter qp) {
		super(qp);
		this.data = data;
	}

	/**
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		return Objects.hashCodeBuilder()
				.appendSuper(super.hashCode())
				.append(data)
				.hashCode();
	}

	/**
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;

		SqlUpdateParameter rhs = (SqlUpdateParameter) obj;
		return Objects.equalsBuilder()
				.appendSuper(super.equals(rhs))
				.append(data, rhs.data)
				.isEquals();
	}

	/**
	 * @return a string representation of the object.
	 */
	@Override
	public String toString() {
		return Objects.toStringBuilder(this)
				.appendSuper(super.toString())
				.append("data", data)
				.toString();
	}

}