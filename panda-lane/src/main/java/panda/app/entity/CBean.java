package panda.app.entity;

import java.util.Date;

import panda.dao.entity.annotation.Column;
import panda.lang.Objects;

public abstract class CBean extends Bean implements ICreate {
	
	@Column(notNull=true)
	protected Long cusid;
	protected String cusnm;
	
	@Column(notNull=true)
	protected Date ctime;
	
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

	//----------------------------------------------------------------------
	/**
	 * set properties from the specified object.
	 */
	public void set(CBean src) {
		this.cusid = src.cusid;
		this.cusnm = src.cusnm;
		this.ctime = src.ctime;
	}

	/**
	 * @return a string representation of the object.
	 */
	@Override
	public String toString() {
		return Objects.toStringBuilder()
				.append(CUSID, cusid)
				.append(CUSNM, cusnm)
				.append(CTIME, ctime)
				.toString();
	}
}

