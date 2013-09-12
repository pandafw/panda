package panda.dao.sql.criteria;

import java.util.List;

import panda.lang.Objects;

/**
 * @author yf.frank.wang@gmail.com
 */
public class SqlSelectParameter extends SqlQueryParameter {

	private SqlSelectClause selectClause;

	private SqlFromClause fromClause;

	/**
	 * @return selectClause
	 */
	public SqlSelectClause getSelectClause() {
		return selectClause;
	}

	/**
	 * @param selectClause the selectClause to set
	 */
	public void setSelectClause(SqlSelectClause selectClause) {
		this.selectClause = selectClause;
	}

	/**
	 * @return fromClause
	 */
	public SqlFromClause getFromClause() {
		return fromClause;
	}

	/**
	 * @param fromClause the fromClause to set
	 */
	public void setFromClause(SqlFromClause fromClause) {
		this.fromClause = fromClause;
	}

	/**
	 * constructor
	 * 
	 * @param qp QueryParameter
	 */
	public SqlSelectParameter(SqlQueryParameter qp) {
		super(qp);
		selectClause = new SqlSelectClause();
		fromClause = new SqlFromClause();
	}

	/**
	 * constructor
	 * 
	 * @param sp SelectParameter
	 */
	public SqlSelectParameter(SqlSelectParameter sp) {
		super(sp);
		this.selectClause = sp.selectClause;
		this.fromClause = sp.fromClause;
	}

	/**
	 * constructor
	 * 
	 * @param selectClause selectClause
	 */
	public SqlSelectParameter(SqlSelectClause selectClause) {
		super();
		this.selectClause = selectClause;
		this.fromClause = new SqlFromClause();
	}

	/**
	 * constructor
	 * 
	 * @param selectClause selectClause
	 * @param qp QueryParameter
	 */
	public SqlSelectParameter(SqlSelectClause selectClause, SqlQueryParameter qp) {
		super(qp);
		this.selectClause = selectClause;
		this.fromClause = new SqlFromClause();
	}

	/**
	 * constructor
	 * 
	 * @param fromClause fromClause
	 */
	public SqlSelectParameter(SqlFromClause fromClause) {
		super();
		this.selectClause = new SqlSelectClause();
		this.fromClause = fromClause;
	}

	/**
	 * constructor
	 * 
	 * @param fromClause fromClause
	 * @param qp QueryParameter
	 */
	public SqlSelectParameter(SqlFromClause fromClause, SqlQueryParameter qp) {
		super(qp);
		this.selectClause = new SqlSelectClause();
		this.fromClause = fromClause;
	}

	/**
	 * constructor
	 * 
	 * @param selectClause selectClause
	 * @param fromClause fromClause
	 */
	public SqlSelectParameter(SqlSelectClause selectClause,
			SqlFromClause fromClause) {
		super();
		this.selectClause = selectClause;
		this.fromClause = fromClause;
	}

	/**
	 * constructor
	 * 
	 * @param selectClause selectClause
	 * @param fromClause fromClause
	 * @param qp QueryParameter
	 */
	public SqlSelectParameter(SqlSelectClause selectClause,
			SqlFromClause fromClause, SqlQueryParameter qp) {
		super(qp);
		this.selectClause = selectClause;
		this.fromClause = fromClause;
	}

	/**
	 * clear
	 */
	public void clear() {
		super.clear();
		if (selectClause != null) {
			selectClause.clear();
		}
	}

	// -------------------------------------------------------------------------
	// shortcuts for ibatis
	// -------------------------------------------------------------------------
	/**
	 * @return column list
	 */
	public List<SelectColumnExpression> getSelectColumns() {
		if (selectClause == null) {
			return null;
		}
		return selectClause.getExpressions();
	}

	/**
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result
				+ ((fromClause == null) ? 0 : fromClause.hashCode());
		result = prime * result
				+ ((selectClause == null) ? 0 : selectClause.hashCode());
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
		SqlSelectParameter other = (SqlSelectParameter) obj;
		if (fromClause == null) {
			if (other.fromClause != null)
				return false;
		} else if (!fromClause.equals(other.fromClause))
			return false;
		if (selectClause == null) {
			if (other.selectClause != null)
				return false;
		} else if (!selectClause.equals(other.selectClause))
			return false;
		return true;
	}

	/**
	 * @return a string representation of the object.
	 */
	@Override
	public String toString() {
		return Objects.toStringBuilder(this)
				.appendSuper(super.toString())
				.append("fromClause", fromClause)
				.append("selectClause", selectClause)
				.toString();
	}

}
