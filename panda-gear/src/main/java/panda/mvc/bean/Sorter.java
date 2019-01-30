package panda.mvc.bean;

import java.io.Serializable;

import panda.lang.Objects;
import panda.lang.Order;
import panda.lang.Strings;
import panda.mvc.annotation.validate.ConstantValidate;


/**
 */
public class Sorter implements Cloneable, Serializable {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * ASC = "asc";
	 */
	public static final String ASC = Order.ASC.toString().toLowerCase();
	
	/**
	 * DESC = "desc";
	 */
	public static final String DESC = Order.DESC.toString().toLowerCase();

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
	@ConstantValidate(list="[ 'asc', 'desc' ]")
	public String getD() {
		return getDirection();
	}
	
	/**
	 * @param direction the direction to set
	 */
	public void setD(String direction) {
		setDirection(direction);
	}

	public void clear() {
		column = null;
		direction = null;
	}
	
	/**
	 * @return a string representation of the object.
	 */
	@Override
	public String toString() {
		return Objects.toStringBuilder()
				.append("column", column)
				.append("direction", direction)
				.toString();

	}

	/**
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		return Objects.hashCodes(column, direction);
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

		Sorter rhs = (Sorter) obj;
		return Objects.equalsBuilder()
				.append(column, rhs.column)
				.append(direction, rhs.direction)
				.isEquals();
	}

	/**
	 * Clone
	 * @return Clone Object
	 */
	public Sorter clone() {
		Sorter clone = new Sorter();
		
		clone.column = this.column;
		clone.direction = this.direction;
		
		return clone;
	}

}
