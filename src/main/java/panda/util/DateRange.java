package panda.util;

import java.io.Serializable;
import java.util.Date;

/**
 * DateRange
 * @author yf.frank.wang@gmail.com
 */
@SuppressWarnings("serial")
public class DateRange implements Cloneable, Serializable {

	private Date from;
	private Date to;

	/**
	 * @return the from
	 */
	public Date getFrom() {
		return from;
	}

	/**
	 * @param from the from to set
	 */
	public void setFrom(Date from) {
		this.from = from;
	}

	/**
	 * @return the to
	 */
	public Date getTo() {
		return to;
	}

	/**
	 * @param to the to to set
	 */
	public void setTo(Date to) {
		this.to = to;
	}

	//------------------------------------------------
	// short name
	//------------------------------------------------
	/**
	 * @return the from
	 */
	public Date getF() {
		return from;
	}

	/**
	 * @param from the from to set
	 */
	public void setF(Date from) {
		this.from = from;
	}

	/**
	 * @return the to
	 */
	public Date getT() {
		return to;
	}

	/**
	 * @param to the to to set
	 */
	public void setT(Date to) {
		this.to = to;
	}

	/**
     * @return  a string representation of the object.
	 */
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();

		sb.append("{ ");
		sb.append("from: ").append(from);
		sb.append(", ");
		sb.append("to: ").append(to);
		sb.append(" }");
		
		return sb.toString();
	}

	/**
     * @return  a hash code value for this object.
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((from == null) ? 0 : from.hashCode());
		result = prime * result + ((to == null) ? 0 : to.hashCode());
		return result;
	}

	/**
     * @return  <code>true</code> if this object is the same as the obj argument; 
     * 			<code>false</code> otherwise.
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DateRange other = (DateRange) obj;
		if (from == null) {
			if (other.from != null)
				return false;
		}
		else if (!from.equals(other.from))
			return false;
		if (to == null) {
			if (other.to != null)
				return false;
		}
		else if (!to.equals(other.to))
			return false;
		return true;
	}

	/**
	 * Clone
	 * @return Clone Object
	 */
	public Object clone() {
		DateRange clone = new DateRange();
		
		clone.from = (Date)this.from.clone();
		clone.to = (Date)this.to.clone();
		
		return clone;
	}

}
