package panda.dao.sql.criteria;


/**
 * @author yf.frank.wang@gmail.com
 */
@SuppressWarnings("serial")
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
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((data == null) ? 0 : data.hashCode());
		return result;
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
		SqlUpdateParameter other = (SqlUpdateParameter) obj;
		if (data == null) {
			if (other.data != null)
				return false;
		} else if (!data.equals(other.data))
			return false;
		return true;
	}

	/**
	 * @return a string representation of the object.
	 */
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();

		sb.append("{ ");
		sb.append(super.toString());
		sb.append(", ");
		sb.append("data: ").append(data);
		sb.append(" }");

		return sb.toString();
	}

}