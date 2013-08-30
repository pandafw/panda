package panda.util;

import java.io.Serializable;

import panda.lang.Strings;


/**
 * @author yf.frank.wang@gmail.com
 */
@SuppressWarnings("serial")
public class Sorter implements Cloneable, Serializable {

	/**
	 * ASC = "ASC";
	 */
	public static final String ASC = "ASC";
	
	/**
	 * DESC = "DESC";
	 */
	public static final String DESC = "DESC";

	/**
	 * constructor
	 */
	public Sorter() {
	}

	private String column;
	private String direction;

	/**
	 * @return the column
	 */
	public String getColumn() {
		return column;
	}
	
	/**
	 * @param column the column to set
	 */
	public void setColumn(String column) {
		this.column = Strings.stripToNull(column);
	}
	
	/**
	 * @return the direction
	 */
	public String getDirection() {
		return direction;
	}
	
	/**
	 * @param direction the direction to set
	 */
	public void setDirection(String direction) {
		if (ASC.equalsIgnoreCase(direction)) {
			this.direction = ASC;
		}
		else if (DESC.equalsIgnoreCase(direction)) {
			this.direction = DESC;
		}
	}

	//----------------------------------------
	// short name
	//----------------------------------------
	/**
	 * @return the column
	 */
	public String getC() {
		return getColumn();
	}
	
	/**
	 * @param column the column to set
	 */
	public void setC(String column) {
		setColumn(column);
	}
	
	/**
	 * @return the direction
	 */
	public String getD() {
		return getDirection();
	}
	
	/**
	 * @param direction the direction to set
	 */
	public void setD(String direction) {
		setDirection(direction);
	}

	/**
     * @return  a string representation of the object.
	 */
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();

		sb.append("{ ");
		sb.append("column: ").append(column);
		sb.append(", ");
		sb.append("direction: ").append(direction);
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
		result = prime * result + ((column == null) ? 0 : column.hashCode());
		result = prime * result + ((direction == null) ? 0 : direction.hashCode());
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
		Sorter other = (Sorter) obj;
		if (column == null) {
			if (other.column != null)
				return false;
		}
		else if (!column.equals(other.column))
			return false;
		if (direction == null) {
			if (other.direction != null)
				return false;
		}
		else if (!direction.equals(other.direction))
			return false;
		return true;
	}

	/**
	 * Clone
	 * @return Clone Object
	 */
	public Object clone() {
		Sorter clone = new Sorter();
		
		clone.column = this.column;
		clone.direction = this.direction;
		
		return clone;
	}

}
