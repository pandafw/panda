package panda.dao.sql.criteria;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author yf.frank.wang@gmail.com
 */
@SuppressWarnings("serial")
public class SqlSelectClause implements Cloneable, Serializable {

	private List<SelectColumnExpression> expressions;

    /**
	 * @return expressionList
	 */
	public List<SelectColumnExpression> getExpressions() {
		return expressions;
	}

	/**
	 * @param expressions the expressions to set
	 */
	public void setExpressions(List<SelectColumnExpression> expressions) {
		this.expressions = expressions;
	}

	/**
     * constructor
     */
    public SqlSelectClause() {
    	expressions = new ArrayList<SelectColumnExpression>();
    }
    
    /**
     * clear
     */
    public void clear() {
    	expressions.clear();
    }

    /**
     * addSelectColumn
     * @param column column
     */
    public void addSelectColumn(String column) {
    	expressions.add(new SelectColumnExpression(column));
    }

    /**
     * addSelectColumn
     * @param column column
     * @param columnAlias columnAlias
     */
    public void addSelectColumn(String column, String columnAlias) {
    	expressions.add(new SelectColumnExpression(column, columnAlias));
    }

	/**
	 * Clone
	 * @throws CloneNotSupportedException if clone not supported
	 * @return Clone Object
	 */
	public Object clone() throws CloneNotSupportedException {
		return super.clone();
	}

	/**
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((expressions == null) ? 0 : expressions.hashCode());
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
		SqlSelectClause other = (SqlSelectClause) obj;
		if (expressions == null) {
			if (other.expressions != null)
				return false;
		}
		else if (!expressions.equals(other.expressions))
			return false;
		return true;
	}

	/**
     * @return  a string representation of the object.
	 */
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();

		sb.append("{ ");
		sb.append("expressions: [ ").append(expressions).append(" ]");
		sb.append(" }");
		
		return sb.toString();
	}

}
