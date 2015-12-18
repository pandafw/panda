package panda.wing.auth;

import java.util.List;

public interface IUser {
	public Long getId();

	public void setId(Long id);

	public String getName();

	public String getPassword();

	public Long getGid();

	public String getGroupName();

	public Integer getGroupLevel();

	public List<String> getGroupPermits();
	
	public Long getLoginTime();
}

