package panda.app.entity;

import java.util.Date;

import panda.dao.entity.annotation.Column;
import panda.lang.Objects;

public abstract class CBean extends Bean implements ICreatedBy {
	
	@Column(notNull=true)
	protected Date createdAt;

	@Column(notNull=true)
	protected Long createdBy;

	protected String createdByName;

	/**
	 * @return the createdAt
	 */
	@Override
	public Date getCreatedAt() {
		return createdAt;
	}

	/**
	 * @param createdAt the createdAt to set
	 */
	@Override
	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}
	
	/**
	 * @return the createdBy
	 */
	@Override
	public Long getCreatedBy() {
		return createdBy;
	}

	/**
	 * @param createdBy the createdBy to set
	 */
	@Override
	public void setCreatedBy(Long createdBy) {
		this.createdBy = createdBy;
	}

	/**
	 * @return the createdByName
	 */
	@Override
	public String getCreatedByName() {
		return createdByName;
	}

	/**
	 * @param createdByName the createdByName to set
	 */
	@Override
	public void setCreatedByName(String createdByName) {
		this.createdByName = createdByName;
	}

	/**
	 * @return createdBy:cusnmr
	 */
	@Override
	public String getCreatedByUser() {
		return createdBy == null ? null : createdBy + (createdByName == null ? "" : ':' + createdByName);
	}

	//----------------------------------------------------------------------
	/**
	 * copy properties from the specified object.
	 * @param src the source object to copy
	 */
	public void copy(CBean src) {
		this.createdAt = src.createdAt;
		this.createdBy = src.createdBy;
		this.createdByName = src.createdByName;
	}

	/**
	 * @return a string representation of the object.
	 */
	@Override
	public String toString() {
		return Objects.toStringBuilder()
				.append(CREATED_AT, createdAt)
				.append(CREATED_BY, createdBy)
				.append(CREATED_BY_NAME, createdByName)
				.toString();
	}
}

