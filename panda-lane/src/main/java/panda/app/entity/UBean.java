package panda.app.entity;

import java.util.Date;

import panda.dao.entity.annotation.Column;
import panda.lang.Objects;

public class UBean extends Bean implements IUpdatedBy {
	
	@Column(notNull=true)
	protected Date updatedAt;
	
	@Column(notNull=true)
	protected Long updatedBy;

	protected String updatedByName;

	/**
	 * @return the updatedBy
	 */
	@Override
	public Long getUpdatedBy() {
		return updatedBy;
	}

	/**
	 * @param updatedBy the updatedBy to set
	 */
	@Override
	public void setUpdatedBy(Long updatedBy) {
		this.updatedBy = updatedBy;
	}

	/**
	 * @return the updatedByName
	 */
	@Override
	public String getUpdatedByName() {
		return updatedByName;
	}

	/**
	 * @param updatedByName the updatedByName to set
	 */
	@Override
	public void setUpdatedByName(String updatedByName) {
		this.updatedByName = updatedByName;
	}

	/**
	 * @return the updatedAt
	 */
	@Override
	public Date getUpdatedAt() {
		return updatedAt;
	}

	/**
	 * @param updatedAt the updatedAt to set
	 */
	@Override
	public void setUpdatedAt(Date updatedAt) {
		this.updatedAt = updatedAt;
	}

	/**
	 * @return updatedBy:uusnmr
	 */
	@Override
	public String getUpdatedByUser() {
		return updatedBy == null ? null : updatedBy + (updatedByName == null ? "" : ':' + updatedByName);
	}

	//----------------------------------------------------------------------
	/**
	 * copy properties from the specified object.
	 * @param src the source object to copy
	 */
	public void copy(UBean src) {
		this.updatedAt = src.updatedAt;
		this.updatedBy = src.updatedBy;
		this.updatedByName = src.updatedByName;
	}

	/**
	 * @return a string representation of the object.
	 */
	@Override
	public String toString() {
		return Objects.toStringBuilder()
				.append(UPDATED_AT, updatedAt)
				.append(UPDATED_BY, updatedBy)
				.append(UPDATED_BY_NAME, updatedByName)
				.toString();
	}
}

