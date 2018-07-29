package panda.app.entity;

import java.util.Date;

public interface IUpdatedBy {
	public static final String UPDATED_AT = "updatedAt";
	public static final String UPDATED_BY = "updatedBy";
	public static final String UPDATED_BY_NAME = "updatedByName";
	public static final String UPDATED_BY_USER = "updatedByUser";

	public Date getUpdatedAt();

	public void setUpdatedAt(Date utime);

	public Long getUpdatedBy();

	public void setUpdatedBy(Long uusid);

	public String getUpdatedByName();

	public void setUpdatedByName(String uusnm);

	public String getUpdatedByUser();

}

