package panda.wing.entity;

import panda.dao.entity.annotation.Column;
import panda.lang.Objects;
import panda.wing.constant.VC;

public class SBean extends Bean implements IStatus {
	
	private static final long serialVersionUID = 1L;

	@Column(notNull=true)
	protected Character status = VC.STATUS_0;
	

	/**
	 * @return the status
	 */
	@Override
	public Character getStatus() {
		return status;
	}

	/**
	 * @param status the status to set
	 */
	@Override
	public void setStatus(Character status) {
		this.status = status;
	}

	//----------------------------------------------------------------------
	/**
	 * is this data enabled
	 * @return true if this data is enabled
	 */
	@Override
	public boolean isValid() {
		return isValid(this);
	}

	/**
	 * is this data disabled
	 * @return true if this data is disabled
	 */
	@Override
	public boolean isInvalid() {
		return isInvalid(this);
	}

	//----------------------------------------------------------------------
	/**
	 * copy properties from the specified object.
	 */
	public void copy(SBean src) {
		this.status = src.status;
	}

	/**
	 * @return a string representation of the object.
	 */
	@Override
	public String toString() {
		return Objects.toStringBuilder()
				.append(STATUS, status)
				.toString();
	}
}

