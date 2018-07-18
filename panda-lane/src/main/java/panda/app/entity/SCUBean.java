package panda.app.entity;

import java.util.Date;

import panda.dao.entity.annotation.Column;
import panda.lang.Objects;

public class SCUBean extends Bean implements IStatus, ICreate, IUpdate {

	@Column(notNull=true)
	protected Character status;
	
	@Column(notNull=true)
	protected Long cusid;
	protected String cusnm;
	
	@Column(notNull=true)
	protected Date ctime;
	
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
	 * @return the cusid
	 */
	@Override
	public Long getCusid() {
		return cusid;
	}

	/**
	 * @param cusid the cusid to set
	 */
	@Override
	public void setCusid(Long cusid) {
		this.cusid = cusid;
	}

	/**
	 * @return the cusnm
	 */
	@Override
	public String getCusnm() {
		return cusnm;
	}

	/**
	 * @param cusnm the cusnm to set
	 */
	@Override
	public void setCusnm(String cusnm) {
		this.cusnm = cusnm;
	}

	/**
	 * @return the ctime
	 */
	@Override
	public Date getCtime() {
		return ctime;
	}

	/**
	 * @param ctime the ctime to set
	 */
	@Override
	public void setCtime(Date ctime) {
		this.ctime = ctime;
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

	/**
	 * @return cusid:cusnmr
	 */
	@Override
	public String getCuser() {
		return cusid == null ? null : cusid + (cusnm == null ? "" : ':' + cusnm);
	}

	/**
	 * @return uusid:uusnmr
	 */
	@Override
	public String getUuser() {
		return uusid == null ? null : uusid + (uusnm == null ? "" : ':' + uusnm);
	}

	//----------------------------------------------------------------------
	/**
	 * copy properties from the specified object.
	 * @param src the source object to copy
	 */
	public void copy(SCUBean src) {
		this.status = src.status;
		this.cusid = src.cusid;
		this.cusnm = src.cusnm;
		this.ctime = src.ctime;
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
				.append(CUSID, cusid)
				.append(CUSNM, cusnm)
				.append(CTIME, ctime)
				.append(UUSID, uusid)
				.append(UUSNM, uusnm)
				.append(UTIME, utime)
				.toString();
	}
}

