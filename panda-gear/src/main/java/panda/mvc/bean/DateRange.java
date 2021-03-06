package panda.mvc.bean;

import java.io.Serializable;
import java.util.Date;

import panda.lang.Objects;
import panda.mvc.annotation.validate.CastErrorValidate;
import panda.mvc.annotation.validate.ELValidate;
import panda.mvc.validator.Validators;

/**
 * DateRange
 */
public class DateRange implements Cloneable, Serializable {
	private static final long serialVersionUID = 2L;

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
	@CastErrorValidate(msgId=Validators.MSGID_DATE)
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
	@CastErrorValidate(msgId=Validators.MSGID_DATE)
	@ELValidate(el="top.parent.value.f == null || top.value > top.parent.value.f", msgId=Validators.MSGID_DATE_TO)
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
	 * @return a string representation of the object.
	 */
	@Override
	public String toString() {
		return Objects.toStringBuilder()
				.append("from", from)
				.append("to", to)
				.toString();
	}

	/**
	 * @return a hash code value for this object.
	 */
	@Override
	public int hashCode() {
		return Objects.hash(from, to);
	}

	/**
	 * @return <code>true</code> if this object is the same as the obj argument; <code>false</code>
	 *         otherwise.
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;

		DateRange rhs = (DateRange) obj;
		return Objects.equalsBuilder()
				.append(from, rhs.from)
				.append(to, rhs.to)
				.isEquals();
	}

	/**
	 * Clone
	 * @return Clone Object
	 */
	public Object clone() {
		DateRange clone = new DateRange();
		
		clone.from = new Date(this.from.getTime());
		clone.to = new Date(this.to.getTime());
		
		return clone;
	}

}
