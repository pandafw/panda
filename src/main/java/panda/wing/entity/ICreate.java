package panda.wing.entity;

import java.util.Date;

public interface ICreate {
	public static final String CUSID = "cusid";
	public static final String CUSNM = "cusnm";
	public static final String CTIME = "ctime";

	public Long getCusid();

	public void setCusid(Long cusid);

	public String getCusnm();

	public void setCusnm(String cusnm);

	public Date getCtime();

	public void setCtime(Date ctime);
}
