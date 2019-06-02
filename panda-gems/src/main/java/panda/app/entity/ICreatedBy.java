package panda.app.entity;

import java.util.Date;

public interface ICreatedBy {
	public static final String CREATED_AT = "createdAt";
	public static final String CREATED_BY = "createdBy";
	public static final String CREATED_BY_NAME = "createdByName";
	public static final String CREATED_BY_USER = "createdByUser";

	public Date getCreatedAt();

	public void setCreatedAt(Date ctime);

	public Long getCreatedBy();

	public void setCreatedBy(Long cusid);

	public String getCreatedByName();

	public void setCreatedByName(String cusnm);

	public String getCreatedByUser();

}

