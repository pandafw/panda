package panda.app.entity;

import panda.dao.entity.annotation.Column;
import panda.lang.Objects;

public class SBean extends Bean implements IStatus {
	
	@Column(notNull=true)
	protected Character status;
	

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

