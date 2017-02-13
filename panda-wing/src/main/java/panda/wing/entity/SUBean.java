package panda.wing.entity;

import java.util.Date;

import panda.dao.entity.annotation.Column;
import panda.lang.Objects;

public class SUBean extends Bean implements IStatus, IUpdate {
	
	@Column(notNull=true)
	protected Character status;
	
	@Column(notNull=true)
	protected Long uusid;
	protected String uusnm;
	
	@Column(notNull=true)
	protected Date utime;


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

	/**
	 * @return the uusid
	 */
	@Override
	public Long getUusid() {
		return uusid;
	}

	/**
	 * @param uusid the uusid to set
	 */
	@Override
	public void setUusid(Long uusid) {
		this.uusid = uusid;
	}

	/**
	 * @return the uusnm
	 */
	@Override
	public String getUusnm() {
		return uusnm;
	}

	/**
	 * @param uusnm the uusnm to set
	 */
	@Override
	public void setUusnm(String uusnm) {
		this.uusnm = uusnm;
	}

	/**
	 * @return the utime
	 */
	@Override
	public Date getUtime() {
		return utime;
	}

	/**
	 * @param utime the utime to set
	 */
	@Override
	public void setUtime(Date utime) {
		this.utime = utime;
	}

	//----------------------------------------------------------------------
	/**
	 * copy properties from the specified object.
	 */
	public void copy(SUBean src) {
		this.status = src.status;
		this.uusid = src.uusid;
		this.uusnm = src.uusnm;
		this.utime = src.utime;
	}

	/**
	 * @return a string representation of the object.
	 */
	@Override
	public String toString() {
		return Objects.toStringBuilder()
				.append(STATUS, status)
				.append(UUSID, uusid)
				.append(UUSNM, uusnm)
				.append(UTIME, utime)
				.toString();
	}
}

