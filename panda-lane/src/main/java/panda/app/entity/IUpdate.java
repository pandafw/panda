package panda.app.entity;

import java.util.Date;

public interface IUpdate {
	public static final String UUSER = "uuser";
	public static final String UUSID = "uusid";
	public static final String UUSNM = "uusnm";
	public static final String UTIME = "utime";

	public String getUuser();

	public Long getUusid();

	public void setUusid(Long uusid);

	public String getUusnm();

	public void setUusnm(String uusnm);

	public Date getUtime();

	public void setUtime(Date utime);
}

