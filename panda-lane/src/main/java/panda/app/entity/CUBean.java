package panda.app.entity;

import java.util.Date;

import panda.dao.entity.annotation.Column;
import panda.lang.Objects;

public class CUBean extends Bean implements ICreatedBy, IUpdatedBy {
	
	@Column(notNull=true)
	protected Date createdAt;

	@Column(notNull=true)
	protected Long createdBy;

	protected String createdByName;
	
	@Column(notNull=true)
	protected Date updatedAt;
	
	@Column(notNull=true)
	protected Long updatedBy;

	protected String updatedByName;


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
	public void copy(CUBean src) {
		this.createdAt = src.createdAt;
		this.createdBy = src.createdBy;
		this.createdByName = src.createdByName;
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
				.append(CREATED_AT, createdAt)
				.append(CREATED_BY, createdBy)
				.append(CREATED_BY_NAME, createdByName)
				.append(UPDATED_AT, updatedAt)
				.append(UPDATED_BY, updatedBy)
				.append(UPDATED_BY_NAME, updatedByName)
				.toString();
	}
}

