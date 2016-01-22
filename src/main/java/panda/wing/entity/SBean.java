package panda.wing.entity;

import panda.dao.entity.annotation.Column;
import panda.lang.Objects;
import panda.wing.constant.VC;

public class SBean extends Bean implements IStatus {
	
	@Column(notNull=true)
	protected Character status = VC.STATUS_ACTIVE;
	

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
	 * is this data active
	 * @return true if bean is valid
	 */
	public boolean isActive() {
		return isActive(this);
	}
	
	/**
	 * is this data disabled
	 * @return true if bean is disabled
	 */
	public boolean isDisabled() {
		return isDisabled(this);
	}
	
	/**
	 * is this data trashed
	 * @return true if bean is trashed
	 */
	public boolean isTrashed() {
		return isTrashed(this);
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

